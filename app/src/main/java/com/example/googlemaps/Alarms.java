package com.example.googlemaps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class Alarms extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener, ResultCallback<Status> {

    private final String DEBUG_TAG = "ALARMS";
    private static String address, Title;
    private static float range;
    private static LatLng latLng;
    private GoogleApiClient mGoogleApiClient;

    /**
     * Used when requesting to add or remove geofences.
     */
    private PendingIntent mGeofencePendingIntent;

    private Dialog mDialog;

    public Alarms() {

    }


    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarms);
        latLng = new LatLng(getIntent().getExtras().getDouble("LATITUDE"), getIntent().getExtras().getDouble("LONGITUDE"));
        address = getIntent().getExtras().getString("ADDRESS");
        TextView location = (TextView) findViewById(R.id.editlocation);
        location.setText(address);
        buildGoogleApiClient();
        mDialog = new Dialog(this);
        mDialog.setTitle("Adding Reminder");
        mDialog.setCancelable(false);
    }

    private void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

    }

    public void onClickbtnSet(View v) {

        EditText title = (EditText) findViewById(R.id.edittitle);
        EditText rangeui = (EditText) findViewById(R.id.editrange);
        Title = title.getText().toString();
        if (rangeui.getText().toString().isEmpty()) {
            AlertDialog alert = new AlertDialog.Builder(Alarms.this).create();
            alert.setTitle("Alert!!");
            alert.setMessage("Please fill in How many metres before the destination you want the alarm to ring..");
            alert.setButton(alert.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.show();

        } else {
            if (Title.isEmpty()) {
                Title = "No todo.";
            }
            range = Float.parseFloat(rangeui.getText().toString());

            mDialog.show();

            addGeofence(createGeofence());
        }
    }

    private Geofence createGeofence() {
        return (new Geofence.Builder().
                setRequestId(Title)
                .setCircularRegion(latLng.latitude,
                        latLng.longitude,
                        range)
                .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setLoiteringDelay(1)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
    }

    private void addGeofence(Geofence geofence) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

            // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
            // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
            // is already inside that geofence.
            builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

            // Add the geofences to be monitored by geofencing service.
            builder.addGeofence(geofence);

            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    builder.build(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().

        } catch (SecurityException securityException) {

            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            Log.d(DEBUG_TAG, securityException.getMessage());
        }
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(DEBUG_TAG, "Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(DEBUG_TAG, "Connection failed!");
    }

    @Override
    public void onResult(Status status) {
        mDialog.dismiss();

        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            SharedPreference sharedPreference = new SharedPreference(this);
            sharedPreference.addAlarmData(Title, address, latLng, range);

            // Update the UI. Adding geofences enables the Remove Geofences button, and removing
            // geofences enables the Add Geofences button.
            Toast.makeText(
                    this,
                    getString(R.string.geofences_added),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
            Toast.makeText(
                    this,
                    errorMessage,
                    Toast.LENGTH_SHORT
            ).show();
            Log.e(DEBUG_TAG, errorMessage);
        }
    }
}
