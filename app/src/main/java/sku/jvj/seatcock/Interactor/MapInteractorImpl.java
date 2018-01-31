package sku.jvj.seatcock.Interactor;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import sku.jvj.seatcock.Activity.MapActivity;
import sku.jvj.seatcock.Interface.Map.MapInteractor;
import sku.jvj.seatcock.Interface.Map.MapPresenter;

/**
 * Created by Android Hong on 2016-10-27.
 */

public class MapInteractorImpl implements MapInteractor,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<LocationSettingsResult> {

    private double storeLatitude;
    private double storeLongitude;
    private double myLatitude;
    private double myLongitude;

    private MapPresenter mapPresenter;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    public MapInteractorImpl(MapPresenter mapPresenter, double latitude, double longitdue) {
        this.mapPresenter = mapPresenter;
        this.storeLatitude = latitude;
        this.storeLongitude = longitdue;
    }

    @Override
    public void createGoogleApi() {
        mGoogleApiClient = new GoogleApiClient.Builder(MapActivity.mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);
    }

    @Override
    public void googleApiDisconnect() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public double getLatitude() {
        return storeLatitude;
    }

    @Override
    public double getLongitude() {
        return storeLongitude;
    }

    @Override
    public double getMyLatitude() {
        return myLatitude;
    }

    @Override
    public double getMyLongitude() {
        return myLongitude;
    }

    @Override
    public void startLocationUpdates() {
        Log.e("실행","실행");
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        result.setResultCallback(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if(mLastLocation != null){
                    myLatitude = mLastLocation.getLatitude();
                    myLongitude = mLastLocation.getLongitude();
                    mapPresenter.laterGPSConnect();
                }else {
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                }
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(MapActivity.mActivity, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        myLatitude =location.getLatitude();
        myLongitude =location.getLongitude();

        mapPresenter.laterGPSConnect();

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
}
