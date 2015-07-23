package com.example.googlemaps;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmReceiver extends Activity {
	
	private MediaPlayer mp;
	private PowerManager.WakeLock mwl;
	public static int ref,setlist,fl;
	
	public static ArrayList<String> title = new ArrayList<String>();
	
	public AlarmReceiver(){
		
	}
	
	public AlarmReceiver(String string){
		title.add(string);
	}

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mwl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Wake Log");
		mwl.acquire();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | 
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
				WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_FULLSCREEN | 
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
				WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		setContentView(R.layout.alarmreceiver);
		TextView Title = (TextView) findViewById(R.id.texttitle);
		Alarms as = new Alarms();
		//ref = as.give();						// Getting the corresponding "INDEX" of the alarm to be rang by the Alarm Service class.
		if(title.size()==0){
			Title.setText("No Title");
		}
		else{
		Title.setText(""+ title.get(ref));
		}
		playSound(this,getAlarmUri());
				
	}
	public int[] get(){						// This method will send the corresponding Alarm's "INDEX" to the AlamrsList class.
		int[] a = {setlist,ref};
		return a;
	}
	

	
	public void OnClickbtnStop(View v){
		mp.stop();
		title.remove(ref);
		setlist=1;
		Intent i = new Intent(AlarmReceiver.this, AlarmsList.class);
		AlarmReceiver.this.startActivity(i);

		fl=1;

		finish();
		
		mwl.release();
		
	}
	
	public void change(){
		setlist=0;
	}
	
	public int give(){				// This method tells the AlarmsService class that whether this class is done with the alarm or not. 
									//  So that the Service can be stopped.
		if(fl==1){
			fl=0;
			return 1;
		}
		else{
		return 0;
		}
	}
	
	private void playSound(Context context,Uri alert){
		mp = new MediaPlayer();
		try{
			mp.setDataSource(context,alert);
			final AudioManager audiomanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			if(audiomanager.getStreamVolume(AudioManager.STREAM_ALARM) !=0){
				mp.setAudioStreamType(AudioManager.STREAM_ALARM);
				mp.prepare();
				mp.start();
			}
		}
		catch(IOException e){
			 Toast mtoast = Toast.makeText(AlarmReceiver.this,"NO AUDIO FILES ARE FOUND!!", Toast.LENGTH_LONG);
			  mtoast.show(); 
		}
	}
	
	
	private Uri getAlarmUri(){
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		if(alert==null){
			alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			if(alert==null){
				alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			}
		}
		return alert;
	}
	


}
