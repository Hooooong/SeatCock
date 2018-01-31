package sku.jvj.seatcock.Interactor;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;

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

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import sku.jvj.seatcock.Activity.LocationActivity;
import sku.jvj.seatcock.Activity.MainActivity;
import sku.jvj.seatcock.Interface.Location.LocationInteractor;
import sku.jvj.seatcock.Interface.Location.LocationPresenter;

/**
 * Created by Android Hong on 2016-10-23.
 */

public class LocationInteractorImpl implements LocationInteractor, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<LocationSettingsResult> {
    private final int REQUEST_CHECK_SETTINGS = 1;
    private final LocationPresenter locationPresenter;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    private double latitude;
    private double longitude;

    public LocationInteractorImpl(LocationPresenter locationPresenter){
        this.locationPresenter = locationPresenter;
        latitude = LocationActivity.mActivity.getIntent().getDoubleExtra("latitude",latitude);
        longitude = LocationActivity.mActivity.getIntent().getDoubleExtra("longitude",longitude);

    }

    @Override
    public void googleApiConnect() {
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void googleApiDisconnect() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())  {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        /*mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);*/
        mLocationRequest.setMaxWaitTime(30 * 1000);
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void refreshLocation() {
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
    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void updateLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void onConnected(Bundle bundle) {
        refreshLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    this.latitude = mLastLocation.getLatitude();
                    this.longitude = mLastLocation.getLongitude();

                    locationPresenter.gpsConnected(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                }
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(LocationActivity.mActivity, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        stopLocationUpdate();

        locationPresenter.gpsConnected(latitude, longitude);
    }

}
