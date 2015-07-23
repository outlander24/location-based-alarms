package com.example.googlemaps;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.googlemaps.Alarms;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;


public class AlarmsList extends ListActivity  {

    public static ArrayList<LatLng> latLngList;
    public static ArrayList<Double> rangeList;
    public static ArrayList<String> addressList;
    private SharedPreferences preferences;
    private int totalCount;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AlarmReceiver ar = new AlarmReceiver();
        latLngList = new ArrayList<LatLng>();
        rangeList = new ArrayList<Double>();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        totalCount = preferences.getInt("ARRAY_SIZE",0);
        if(totalCount != 0)
        {
            for(int i=0;i<totalCount;i++)
            {
                latLngList.add(new LatLng(preferences.getFloat("LATITUDE_"+i,0),preferences.getFloat("LONGITUDE_"+i,0)));
                rangeList.add((Double.parseDouble(preferences.getString("RANGE"+i,""))));
            }
        }
		int refer[] = ar.get();
		int set1 = refer[0];				// Getting the corresponding "index" of the alarm if this service is called upon by the AlarmReceiver class.
		int index = refer[1];
		if(set1==1){							// If this activity is called by the AlarmReceiver class then remove the corresponding details of the
												//  alarm that has expired
			ar.change();
			//todo
			// alarmstitleloc.remove(index);
					
//			todo
//          if(alarmstitleloc.isEmpty()){		// If there are no pending alarms then switch off the "Alarm Service".
//
//				finish();
//			}
			
//			else{								// If there are pending alarms then re-Switch the Alarm service ON.
//			Intent ser1 = new Intent(AlarmsList.this,AlarmService.class );
//			AlarmsList.this.startService(ser1);
//			finish();
//			}
		}
		else {									// If this activity is called upon by the user then display the lists of the available alarms.
		//todo
		// setListAdapter(new ArrayAdapter<String>(AlarmsList.this, android.R.layout.simple_list_item_1, alarmstitleloc));
//		Intent service = new Intent(AlarmsList.this, AlarmService.class);
//		AlarmsList.this.startService(service);
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cool_menu, menu);
		return true;
	}
	

	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		
		case R.id.setalarm:
			Intent i2 = new Intent(AlarmsList.this, MainAct.class);

			break;
			
		case R.id.rateandreview:
			Intent ir = new Intent(AlarmsList.this, About.class);
			AlarmsList.this.startActivity(ir);
			
			break;
			
		case R.id.exit:
		 	finish();
			break;
		}
		return false;
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		final int index = position;
		AlertDialog alert = new AlertDialog.Builder(AlarmsList.this).create();
		alert.setTitle("Delete Alarm");
		alert.setMessage("Do you really want to delete this alarm?");
		alert.setButton(alert.BUTTON_NEUTRAL, "Delete", new DialogInterface.OnClickListener() {
					
			public void onClick(DialogInterface dialog, int which) {
							// todo
							//alarmstitleloc.remove(index);
					
					//		AlarmService as = new AlarmService();	// The corresponding details of the alarms in the Alarm Service class also needs 
																	//  to be deleted. Hence, this function call.
						//	as.interrupt(index);
							//todo
							// setListAdapter(new ArrayAdapter<String>(AlarmsList.this, android.R.layout.simple_list_item_1, alarmstitleloc));

			}
		});
		alert.setButton(alert.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
							
			}
		});
		alert.show();
		
	}
	

   public void refresh(int index){				// If a user deletes an alarm from the list then the list is updated using this method.
	    // todo
	    // alarmstitleloc.remove(index);

   }
 



}