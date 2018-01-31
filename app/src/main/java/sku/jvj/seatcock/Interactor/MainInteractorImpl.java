package sku.jvj.seatcock.Interactor;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.ArrayList;

import sku.jvj.seatcock.Activity.MainActivity;
import sku.jvj.seatcock.Interface.Main.MainInteractor;
import sku.jvj.seatcock.Interface.Main.MainPresenter;
import sku.jvj.seatcock.Model.Advertising;
import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Server.AdvertisingData;
import sku.jvj.seatcock.Server.MainData;
import sku.jvj.seatcock.Server.UserData;
import sku.jvj.seatcock.Util.NoticeDBHelper;

/**
 * Created by Android Hong on 2016-10-21.
 */
public class MainInteractorImpl implements MainInteractor,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<LocationSettingsResult> {

    private int pushRequestCode = 0;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private MainPresenter mainPresenter;
    private MainData mainData;
    private AdvertisingData advertisingData;
    private MainHandler handler;
    private double latitude;
    private double longitude;
    private double changeLatitude;
    private double changeLogitude;
    private KakaoUser kakaoUser;

    NoticeDBHelper noticeDbHelper = new NoticeDBHelper(MainActivity.mContext, "tbl_noti.db",null, 1);


    public MainInteractorImpl(MainPresenter mainPresenter){
        this.mainPresenter = mainPresenter;
        pushRequestCode = MainActivity.mActivity.getIntent().getIntExtra("pushRequestCode",pushRequestCode);

        handler = new MainHandler();
    }

    @Override
    public void createGoogleApi() {
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setMaxWaitTime(30 * 1000);
    }

    @Override
    public void googleApiDisconnect() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        result.setResultCallback(this);
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public double getChangeLatitude() {
        return changeLatitude;
    }

    @Override
    public double getChangeLongitude() {
        return changeLogitude;
    }

    @Override
    public void setChangeLatitude(double latitude) {
        this.changeLatitude = latitude;
    }

    @Override
    public void setChangeLongitude(double longitude) {
        this.changeLogitude = longitude;
    }

    @Override
    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public int getNotiCount() {
        return noticeDbHelper.getNumber();
    }

    @Override
    public void getUser() {
        UserManagement.requestMe(new MeResponseCallback() {
            //세션이 닫혀 실패한 경우로 에러 결과를 받습니다.
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("Main : OnSessionClosed", errorResult.toString());
                kakaoUser = null;
                checkIntent();
                mainPresenter.login(kakaoUser);
            }

            //NONE	사용자가 가입된 상태가 아니여서 실패한 경우입니다.
            @Override
            public void onNotSignedUp() {
                Log.e("Main : onNotSignedUp", "가입된 상태가 아닙니다.");
                kakaoUser = null;
                Session.getCurrentSession().close();
                mainPresenter.login(kakaoUser);
            }

            //사용자 정보 요청이 성공한 경우로 사용자 정보 객체를 받습니다.
            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.e("Main : onSuccess", "Login");
                kakaoUser = KakaoUser.getInstance();
                kakaoUser.setId(Long.toString(userProfile.getId()));
                kakaoUser.setNickname(userProfile.getNickname());

                if (userProfile.getProfileImagePath() == null || userProfile.getProfileImagePath().isEmpty()) {
                    // 이미지 존재하지 않음.
                    kakaoUser.setProfile_image("noimage");
                } else {
                    // 이미지 존재
                    kakaoUser.setProfile_image(userProfile.getProfileImagePath());
                }

                kakaoUser.setName(userProfile.getProperty("name"));
                kakaoUser.setPhoneNumber(userProfile.getProperty("phoneNumber"));
                kakaoUser.setJubun(Integer.parseInt(userProfile.getProperty("jubun")));
                kakaoUser.setToken(FirebaseInstanceId.getInstance().getToken());

                // 토큰 업데이트
                UserData.updateToUser(kakaoUser.getId(),kakaoUser.getToken());

                // 로그인 한 후 예약화면, 대기번호화면으로 넘어갈 지 체크
                checkIntent();
                mainPresenter.login(kakaoUser);
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e("Main : onFailure", errorResult.toString());
                kakaoUser = null;

                mainPresenter.login(kakaoUser);
            }
        });
    }

    @Override
    public void logoutUser() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            // 로그아웃을 성공한 경우 불립니다.
            // 서버에 로그아웃 도달과 무관하게 항상 성공을 한다.
            @Override
            public void onCompleteLogout() {
                UserData.deleteToUser(kakaoUser.getId());
                kakaoUser = null;
            }
        });
    }

    @Override
    public void checkIntent() {
        if(pushRequestCode == 100 || pushRequestCode == 200) {
                /* 예약일 경우 */        /* 대기번호 일 경우*/
            mainPresenter.laterToCheckIntent(pushRequestCode, kakaoUser.getId());
        }
    }

    @Override
    public void getStoreData(double latitude, double longitude) {
        mainData = new MainData(handler);
        mainData.getData(latitude,longitude);
    }


    @Override
    public void getAdData() {
        advertisingData = new AdvertisingData(handler);
        advertisingData.getData();
    }

    @Override
    public ArrayList<Store> setStoreData() {
        return   mainData.setData();
    }

    @Override
    public ArrayList<Advertising> setAdData() {
        return advertisingData.setData();
    }

    @Override
    public String getKakaoProfile() {
        return kakaoUser.getProfile_image();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        this.onGPS();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                this.startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(MainActivity.mActivity, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        changeLatitude = latitude;
        changeLogitude = longitude;

        stopLocationUpdate();

         mainPresenter.getLocation(latitude,longitude);
    }

    /**
     * 핸들러 클래스
     */
    public class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STORE_NETWORK_SUCCESS:
                    mainPresenter.onSuccess("store");
                    break;
                case STORE_NETWORK_FAILER:
                    mainPresenter.failOfTimeOut("store");
                    break;
                case AD_NETWORK_SUCCESS:
                    mainPresenter.onSuccess("Advertising");
                    break;
                case AD_NETWORK_FAILER:
                    mainPresenter.failOfTimeOut("Advertising");
                    break;
            }
        }
    }

}
