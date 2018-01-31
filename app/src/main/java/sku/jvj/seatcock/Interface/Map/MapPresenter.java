package sku.jvj.seatcock.Interface.Map;

/**
 * Created by Android Hong on 2016-10-27.
 */

public interface MapPresenter {

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


    void laterGPSConnect();
}
