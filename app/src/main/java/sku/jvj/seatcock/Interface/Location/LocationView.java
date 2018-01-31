package sku.jvj.seatcock.Interface.Location;

/**
 * Created by Android Hong on 2016-10-22.
 */

public interface LocationView {

    /**
     * 내위치(TextView)를 보여주는 메소드
     * @param latitude
     * @param longitude
     */
    void displayLocationText(double latitude, double longitude);

    /**
     * 내위치(mapView)를 보여주는 메소드
     * @param latitude
     * @param longitude
     */
    void displayLocationMap(double latitude, double longitude);

    /**
     * 위치 지정하고 MainActivity로 넘어가는 메소드
     * @param latitude
     * @param longitude
     */
    void navigationToMain(double latitude, double longitude);
}
