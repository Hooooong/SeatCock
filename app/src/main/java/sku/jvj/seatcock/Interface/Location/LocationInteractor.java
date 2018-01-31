package sku.jvj.seatcock.Interface.Location;

/**
 * Created by Android Hong on 2016-10-23.
 */

public interface LocationInteractor {

    void googleApiConnect();
    void googleApiDisconnect();
    void createLocationRequest();

    double getLatitude();
    double getLongitude();

    void refreshLocation();
    void startLocationUpdates();
    void stopLocationUpdate();

    void updateLocation(double latitude, double longitude);
}
