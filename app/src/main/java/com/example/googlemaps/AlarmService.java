package com.example.googlemaps;

import java.util.ArrayList;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;

import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class AlarmService extends Service implements LocationListener {
	int status;
	static int ref,done=0;
	double MyLatitude=0, MyLongitude=0;
	
	
	public static ArrayList<String> alarmstitleloc = new ArrayList<String>();
	public static ArrayList<Double> alarmsrange = new ArrayList<Double>();
	public static ArrayList<LatLng> alarmslatlng = new ArrayList<LatLng>();


	public AlarmService(){
	}
	
	public AlarmService( double range, String location, String title){
		alarmstitleloc.add(title + "\n\nLocation: \n" + location);
		alarmsrange.add(range);
//		alarmslatlng.add(latlng);

	}
	
	public AlarmService (LatLng latlng){
		alarmslatlng.add(latlng);

	}
	
	public IBinder onBind(Intent intent) {
		return null;
	}
	

	public void onCreate() {
		FindMe();

		super.onCreate();
				
	}


	public void onDestroy() {
//		alarmstitleloc.clear();
//		alarmsrange.clear();
//		alarmslatlng.clear();\
		stopSelf();
	}


	public int onStartCommand(Intent intent, int flags, int startId) {
		
		return START_STICKY;
	}

	public void interrupt1(int i){			// This method is called when an alarm has expired and hence it has to be removed from the list.
		int temp =i;
		AlarmReceiver ar = new AlarmReceiver();
		int flag = ar.give();				// Checks if the user has clicked the "Stop" Button to stop the Alarm.
		
		if(flag==1){
		alarmstitleloc.remove(i);
		alarmsrange.remove(i);
		alarmslatlng.remove(i);
		}
		
		else if(flag==0){
			interrupt1(temp);
		}
		
		if(alarmstitleloc.size()==0){
			onDestroy();
		}
		}
	
	public void interrupt(int i){				// This method is called by the AlarmsList class if the user has deleted an alarm before it's expiration.
		alarmstitleloc.remove(i);
		alarmsrange.remove(i);
		alarmslatlng.remove(i);
		if(alarmstitleloc.size()==0){
			onDestroy();
		}
	}
	
	public int give(){					// This method gives the corresponding "INDEX" of the alarm to be rang, to the AlarmReceiver class.
		return ref;
	}

	
	public void FindMe(){   
		   
	       
		   if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
				  
			   Toast mtoast = Toast.makeText(AlarmService.this,"Google-Play Services are not available!!" , Toast.LENGTH_LONG);
			   mtoast.show(); 
		   }
	           
	     else{
	 
	       // Getting LocationManager object from System Service LOCATION_SERVICE
	       LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	       
	       // Creating a criteria object to retrieve provider
	       Criteria criteria = new Criteria();
	     //  criteria.setAccuracy(Criteria.ACCURACY_FINE);

	       // Getting the name of the best provider
	       String provider = locationManager.getBestProvider(criteria, true);

	       // Getting Current Location
	       Location location = locationManager.getLastKnownLocation(provider);
         //  onLocationChanged(location);

		
	       if(location!=null){
	           onLocationChanged(location);
	       }
	       locationManager.requestLocationUpdates(provider, 20000,0, this);      //requesting MyLocation Updates in every 20 seconds.    

	     }

	   }

	@Override
	public void onLocationChanged(Location location) {
		MyLatitude = location.getLatitude();
		MyLongitude = location.getLongitude();
		LatLng MyLatlng = new LatLng(MyLatitude,MyLongitude);
		
//		if(stp==0){
		int i=0;
		while(i<alarmstitleloc.size()){

			LatLng latlng = alarmslatlng.get(i);
			double dist = distance(MyLatlng,latlng);
			
			if(dist<=alarmsrange.get(i)){
				
				ref=i;
				Intent myIntent = new Intent();
				myIntent.setClass(getBaseContext(), AlarmReceiver.class);
				myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(myIntent);
				interrupt1(i);
		/*		alarmstitleloc.remove(i);
				alarmsrange.remove(i);
				alarmslatlng.remove(i);
				onDestroy();*/
							
				break;
				
		}
			i++;
		}


	}
	
	public double distance(LatLng StartP, LatLng EndP) {
		double Radius = 6371;
	    double lat1 = StartP.latitude/1E6;
	    double lat2 = EndP.latitude/1E6;
	    double lon1 = StartP.longitude/1E6;
	    double lon2 = EndP.longitude/1E6;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLon = Math.toRadians(lon2-lon1);
	    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
	            + Math.cos(Math.toRadians(lat1))
	            * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
	            * Math.sin(dLon / 2);
	            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	            double temp = Radius * c;
	            temp=temp*0.621;
	            return temp;
	}
	@Override
	public void onProviderDisabled(String arg0) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
