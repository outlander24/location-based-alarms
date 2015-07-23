package com.example.googlemaps;

import java.util.ArrayList;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Alarms extends Activity {

    public static String address, Title;
    private static float range;
    private static LatLng latLng;

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
                Title = "No Title.";
            }
            range = Float.parseFloat(rangeui.getText().toString());
            SharedPreference sharedPreference = new SharedPreference(this);
            sharedPreference.AddAlarmData(Title, address,latLng, range);
            Toast.makeText(Alarms.this, "Alarm has been set..:)", Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
