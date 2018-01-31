package sku.jvj.seatcock.Presenter;

import sku.jvj.seatcock.Interactor.LocationInteractorImpl;
import sku.jvj.seatcock.Interface.Location.LocationInteractor;
import sku.jvj.seatcock.Interface.Location.LocationPresenter;
import sku.jvj.seatcock.Interface.Location.LocationView;

/**
 * Created by Android Hong on 2016-10-22.
 */
public class LocationPresenterImpl implements LocationPresenter {

    private LocationView view;
    private LocationInteractor locationInteractor;

    public LocationPresenterImpl(LocationView view){
        this.view = view;
        locationInteractor = new LocationInteractorImpl(this);


    }

    @Override
    public void googleApiConnect() {
        locationInteractor.googleApiConnect();
        locationInteractor.createLocationRequest();
    }

    @Override
    public void googleApiDisconnect() {
        locationInteractor.googleApiDisconnect();

    }

    @Override
    public void gpsRefresh() {
        locationInteractor.startLocationUpdates();
    }

    @Override
    public void gpsConnected(double latitude, double longitude) {
        view.displayLocationMap(latitude,longitude);
    }

    @Override
    public void updateLocation(double latitude, double longitude) {
        locationInteractor.updateLocation(latitude,longitude);
    }

    @Override
    public void presentLocation() {
        view.navigationToMain(locationInteractor.getLatitude(),locationInteractor.getLongitude());
    }

    @Override
    public double getLatitude() {
        return locationInteractor.getLatitude();
    }

    @Override
    public double getLongitude() {
        return locationInteractor.getLongitude();
    }

}
