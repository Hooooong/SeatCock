package sku.jvj.seatcock.Interface.Map;

/**
 * Created by Android Hong on 2016-10-27.
 */

public interface MapInteractor {

    int REQUEST_CHECK_SETTINGS = 1;

    void createGoogleApi();
    void createLocationRequest();
    void googleApiDisconnect();

    double getLatitude();
    double getLongitude();

    double getMyLatitude();
    double getMyLongitude();

    void startLocationUpdates();


}
