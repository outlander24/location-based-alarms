package com.example.googlemaps.StaleClasses;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by ashishtotla on 30-03-2015.
 */
public class GetCurrentLocation implements LocationListener {

    private static Location _currentLocation;
    private static LocationManager _locationManager;
    private static String _locationProvider;

    public Location GetLocation(Context context){
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available

            Toast mtoast = Toast.makeText(context,"Google-Play Services are not available!!" , Toast.LENGTH_LONG);
            mtoast.show();
        }
        else{
            // Getting LocationManager object from System Service LOCATION_SERVICE
            _locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();
            //       criteria.setAccuracy(Criteria.ACCURACY_FINE);

            // Getting the name of the best provider
            _locationProvider = _locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            _currentLocation = _locationManager.getLastKnownLocation(_locationProvider);
            System.out.println(_currentLocation.getLatitude());

            if(_currentLocation!=null){
                onLocationChanged(_currentLocation);
            }
            _locationManager.requestLocationUpdates(_locationProvider, 20000,0, this );      //requesting MyLocation Updates in every 20 seconds.

        }
        return _currentLocation;
    }

    public void onLocationChanged(Location location) {

    }

    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    public void onProviderEnabled(String s) {

    }

    public void onProviderDisabled(String s) {

    }
}
