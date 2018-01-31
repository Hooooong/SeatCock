package sku.jvj.seatcock.Interface.Map;

/**
 * Created by Android Hong on 2016-10-27.
 */

public interface MapView {

    void showProgress();
    void hideProgress();

    /**
     * 점포위치(TextView)를 보여주는 메소드

     */
    void displayLocationText(double storeLatitude, double storeLongitude);

    /**
     * 점포 및 내 위치(mapView)를 보여주는 메소드

     */
    void displayLocationMap(double storeLatitude, double storeLongitude,double myLatitude, double myLongitude);

}
