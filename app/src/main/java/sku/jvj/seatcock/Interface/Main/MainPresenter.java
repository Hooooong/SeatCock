package sku.jvj.seatcock.Interface.Main;

import sku.jvj.seatcock.Model.KakaoUser;

/**
 * Created by Android Hong on 2016-10-21.
 */

public interface MainPresenter {

    void loginCheck();
    void login(KakaoUser kakaoUser);
    void logout();

    int getNotiCount();
    /**
     * 초기화 시 ApiConnect, LocationRequest 연결하는 메소드
     */
    void createGoogleApi();

    /**
     * Api 연결 해제하는 메소드
     */
    void googleApiDisconnect();

    /**
     * GPS 연결 안했을 시 GPS 연결하는 메소드
     */
    void gpsConnect();
    /**
     *
     */
    void getLocation(double latitude, double longitude);

    /**
     * 현재 내 위치 가져오는 메소드
     */
    void presentLocation();

    /**
     * 내 위치 업데이트
     * @param latitude
     * @param longitude
     */
    void updateLocation(double latitude, double longitude);

    /**
     * 내 위치 받아와 view(TextView) 에 넘겨주고 Interactor에서 점포정보 받아오는 메소드
     * @param latitude
     * @param longitude
     */
    void getStoreData(double latitude, double longitude);


    /**
     * IntentCheck 후 어떻게 할 것인지
     * @param pushRequestCode
     * @param kakaoId
     */
    void laterToCheckIntent(int pushRequestCode, String kakaoId);

    /**
     * 광고 가져오는 메소드
     */
    void getAdData();

    /**
     * 점포정보 및 광고 데이터를 가져오는데 성공하면

     * @param dataName
     */
    void onSuccess(String dataName);

    /**
     * 점포정보 및 광고 데이터를 가져오는데 실패하면
     * @param dataName
     */
    void failOfTimeOut(String dataName);

    String getKakaoPrifile();

    double getLatitude();

    double getLongitude();


}
