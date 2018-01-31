package sku.jvj.seatcock.Interface.Location;

/**
 * Created by Android Hong on 2016-10-22.
 */

public interface LocationPresenter {

    /**
     * 초기화 시 ApiConnect, LocationRequest 연결하는 메소드
     */
    void googleApiConnect();

    /**
     * Api 연결 해제하는 메소드
     */
    void googleApiDisconnect();

    /**
     * GPS 연결을 했을 GPS가 꺼져 권한설정 OK누른 후 메소드
     */
    void gpsRefresh();

    /**
     * 새로고침 끝나고 메소드
     * @param latitude
     * @param longitude
     */
    void gpsConnected(double latitude, double longitude);

    void updateLocation(double latitude, double longitude);

    /**
     * 내 위치 받아오기
     */
    void presentLocation();

    double getLatitude();

    double getLongitude();
}
