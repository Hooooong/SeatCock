package sku.jvj.seatcock.Presenter;

import sku.jvj.seatcock.Activity.MapActivity;
import sku.jvj.seatcock.Interactor.MapInteractorImpl;
import sku.jvj.seatcock.Interface.Map.MapInteractor;
import sku.jvj.seatcock.Interface.Map.MapPresenter;
import sku.jvj.seatcock.Interface.Map.MapView;

/**
 * Created by Android Hong on 2016-10-27.
 */

public class MapPresenterImpl implements MapPresenter {


    private MapView view;
    private MapInteractor mapInteractor;

    public MapPresenterImpl(MapView view) {
        this.view = view;
        double latitude = 0;
        double longitude = 0;

        latitude = MapActivity.mActivity.getIntent().getDoubleExtra("x",latitude);
        longitude = MapActivity.mActivity.getIntent().getDoubleExtra("y",longitude);

        mapInteractor = new MapInteractorImpl(this, latitude, longitude);
    }

    @Override
    public void createGoogleApi() {
        mapInteractor.createGoogleApi();
        mapInteractor.createLocationRequest();
    }

    @Override
    public void googleApiDisconnect() {
        mapInteractor.googleApiDisconnect();
    }

    @Override
    public void gpsConnect() {
        mapInteractor.startLocationUpdates();
    }

    @Override
    public void laterGPSConnect() {
        if(view!= null){
            view.displayLocationMap(mapInteractor.getLatitude(),mapInteractor.getLongitude(), mapInteractor.getMyLatitude(), mapInteractor.getMyLongitude());
            view.displayLocationText(mapInteractor.getLatitude(),mapInteractor.getLongitude());
        }
    }
}
