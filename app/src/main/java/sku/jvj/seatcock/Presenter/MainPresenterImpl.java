package sku.jvj.seatcock.Presenter;

import android.util.Log;

import sku.jvj.seatcock.Interactor.MainInteractorImpl;
import sku.jvj.seatcock.Interface.Main.MainInteractor;
import sku.jvj.seatcock.Interface.Main.MainPresenter;
import sku.jvj.seatcock.Interface.Main.MainView;
import sku.jvj.seatcock.Model.KakaoUser;

/**
 * Created by Android Hong on 2016-10-21.
 */
public class MainPresenterImpl implements MainPresenter {

    private MainView view;
    private MainInteractor mainIntercator;

    public MainPresenterImpl(MainView view) {
        this.view = view;
        mainIntercator = new MainInteractorImpl(this);

        this.createGoogleApi();
        this.loginCheck();
        this.getAdData();

    }

    @Override
    public void loginCheck() {
         mainIntercator.getUser();
    }

    @Override
    public void login(KakaoUser kakaoUser) {
        view.isLogin(kakaoUser);
    }

    @Override
    public void logout() {
        mainIntercator.logoutUser();
        view.showToast("로그아웃 되었습니다");
        view.isLogin(null);
    }

    @Override
    public void laterToCheckIntent(int pushRequestCode, String kakaoId) {
        if(kakaoId != null){
            if(pushRequestCode == 100){
                view.navigateToMyReservation();
            }else if (pushRequestCode == 200) {
                // 대기번호 화면으로 넘어가야함
                // view.navigateToWaitingTicket();
            }
        }else{
            view.showToast("로그인을 먼저 해주세요");
        }

    }

    @Override
    public int getNotiCount() {
        return mainIntercator.getNotiCount();
    }

    @Override
    public void createGoogleApi() {
        if(view != null){
            view.showProgress();
        }
        mainIntercator.createGoogleApi();
        mainIntercator.createLocationRequest();

    }

    @Override
    public void googleApiDisconnect() {
        mainIntercator.googleApiDisconnect();
    }

    @Override
    public void gpsConnect() {
        if(view != null){
            view.showProgress();
        }
        mainIntercator.onGPS();
    }

    @Override
    public void getLocation(double latitude, double longitude) {
        if(view != null){
            view.hideProgress();
        }
        this.getStoreData(latitude,longitude);
    }

    @Override
    public void presentLocation() {
        view.navigateToLocation(mainIntercator.getChangeLatitude(),mainIntercator.getChangeLongitude());
    }

    @Override
    public void updateLocation(double latitude, double longitude) {
        mainIntercator.setChangeLatitude(latitude);
        mainIntercator.setChangeLongitude(longitude);
    }

    @Override
    public void getStoreData(double latitude, double longitude) {
        if (view != null) {
            view.showProgress();
            view.displayLocationText(latitude,longitude);
        }
        mainIntercator.getStoreData(latitude, longitude);
    }

    @Override
    public void getAdData() {
        mainIntercator.getAdData();
    }

    @Override
    public void onSuccess(String dataName) {
        if (view != null) {
            if (dataName.equals("Advertising")) {
                view.displayAdvertising(mainIntercator.setAdData());
            } else if (dataName.equals("store")) {
                view.displayStore(mainIntercator.setStoreData());
                view.hideProgress();
            }
        }
    }

    @Override
    public void failOfTimeOut(String dataName) {
        if (view != null) {
            view.hideProgress();
        }
    }

    @Override
    public String getKakaoPrifile() {
        return mainIntercator.getKakaoProfile();
    }

    @Override
    public double getLatitude() {
        return mainIntercator.getLatitude();
    }

    @Override
    public double getLongitude() {
        return mainIntercator.getLongitude();
    }

}
