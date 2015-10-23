package com.example.googlemaps;

import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
* Created by ashish on 31/07/15.
 * */

public class LocationHelper implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    private MainAct mMainAct;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public LocationHelper(MainAct a) {
        mGoogleApiClient = new GoogleApiClient.Builder(a)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10 * 1000);
        mLocationRequest.setNumUpdates(1);
        mLocationRequest
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mMainAct = a;

    }

    public void fetchLocation() {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Toast.makeText(mMainAct, "Cannot connect to GoogleApiClient to get location.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this); // LocationListener

    }

    @Override
    public void onConnectionSuspended(int cause) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChanged(Location location) {
        mMainAct.startGettingAddress(location);
        mGoogleApiClient.disconnect();
    }

}
