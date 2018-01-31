package sku.jvj.seatcock.Interface.Main;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.Advertising;
import sku.jvj.seatcock.Model.Store;

/**
 * Created by Android Hong on 2016-10-21.
 */

public interface MainInteractor {
    int REQUEST_CHECK_SETTINGS = 1;
    int STORE_NETWORK_SUCCESS = 1;
    int STORE_NETWORK_FAILER = 2;
    int AD_NETWORK_SUCCESS = 3;
    int AD_NETWORK_FAILER = 4;

    void createGoogleApi();
    void createLocationRequest();
    void googleApiDisconnect();

    void onGPS();

    double getLatitude();
    double getLongitude();

    double getChangeLatitude();
    double getChangeLongitude();
    void setChangeLatitude(double latitude);
    void setChangeLongitude(double longitude);

    void startLocationUpdates();
    void stopLocationUpdate();

    int getNotiCount();
    void getUser();
    void logoutUser();
    void checkIntent();

    void getStoreData(double latitude, double longitude);
    void getAdData();
    ArrayList<Store> setStoreData();
    ArrayList<Advertising> setAdData();

    String getKakaoProfile();
}
