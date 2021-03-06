package com.example.googlemaps;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.googlemaps.GetCurrentAddress.onLocationFetchedListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainAct extends FragmentActivity {

    private GoogleMap map;
    //
    private static LatLng latLng;
    private static float range = 0;
    //
    int status, flag = 0, set = 0, ref = 0;
    private int totalCount = 0;
    private static EditText editTextAddress;
    private SharedPreferences preferences;
    private ProgressDialog mProgressDialog;
    private onLocationFetchedListener mOnLocationFetchedListener;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        editTextAddress = (EditText) findViewById(R.id.AddBar);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Please wait");
        mProgressDialog.setMessage("Fetching your location");
        setUpOnLocationFetchedListener();
        // Getting reference to the SupportMapFragment of activity_main.xml

        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else {
            // Google Play Services are available
            // Getting GoogleMap object from the fragment
            map = fm.getMap();
            mProgressDialog.show();
            LocationHelper getCurrentLocation = new LocationHelper(this);
            getCurrentLocation.fetchLocation();

        }
    }

    private void setUpOnLocationFetchedListener() {
        mOnLocationFetchedListener = new onLocationFetchedListener() {
            @Override
            public void onLocationFetched(String result) {
                mProgressDialog.dismiss();
                editTextAddress.setText(result);
            }
        };
    }

    public void startGettingAddress(Location currentLocation) {
        (new GetCurrentAddress(this, mOnLocationFetchedListener)).execute(currentLocation);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
        map.animateCamera(CameraUpdateFactory.zoomTo(6));
        map.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
    }

    public void SetCurrentAddress(String currentAddress) {

    }

    public void OnClickFind(View v) {

        if (editTextAddress.getText().toString().isEmpty()) {
            Toast.makeText(MainAct.this, "Please enter some detail in text box and try again..", Toast.LENGTH_LONG).show();

        } else if (!(editTextAddress.getText().toString().isEmpty())) {
            flag = 1;
            new FindPlace().execute(editTextAddress.getText().toString().replace(' ', '+'));
        }
    }

    private class FindPlace extends AsyncTask<String, Void, LatLng> {

        String add = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        protected LatLng doInBackground(String... url) {
            String uri = "http://maps.google.com/maps/api/geocode/json?address=" + url[0] + "&sensor=false";
            HttpGet httpGet = new HttpGet(uri);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            StringBuilder stringBuilder = new StringBuilder();
            try {
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(stringBuilder.toString());

                double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

                add = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getString("formatted_address");
                LatLng ll = new LatLng(lat, lng);
                return ll;

            } catch (JSONException e) {

                return null;
            }
        }

        protected void onPostExecute(LatLng result) {
            mProgressDialog.dismiss();
            latLng = result;
            if (result != null) {
                if (add == null) {
                    Toast.makeText(MainAct.this, "No Location Found..!!..Try with some other name..", Toast.LENGTH_SHORT).show();
                } else {
                    editTextAddress.setText(add);
                    map.moveCamera(CameraUpdateFactory.newLatLng(result));
                    map.animateCamera(CameraUpdateFactory.zoomTo(6));
                    map.addMarker(new MarkerOptions().position(new LatLng(result.latitude, result.longitude)));
                    Toast.makeText(MainAct.this, add, Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(MainAct.this, "Check your Internet Settings..", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void OnClickSetAlarm(View v) {
        String address = editTextAddress.getText().toString();

        if (address.isEmpty()) {
            Toast.makeText(MainAct.this, "Please enter some detail in text box and try again..", Toast.LENGTH_LONG).show();

        } else {
            Intent i = new Intent(MainAct.this, Alarms.class);
            i.putExtra("LATITUDE", latLng.latitude);
            i.putExtra("LONGITUDE", latLng.longitude);
            i.putExtra("ADDRESS", address);
            MainAct.this.startActivity(i);
        }
    }

}


