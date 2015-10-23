package com.example.googlemaps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by ashishtotla on 02-04-2015.
 */
public class CardClass extends Activity {

    private Context context;
    private static ArrayList<String> address, title, todo;
    private static MyAdapter myAdapter;
    private static ListView listView;
    private static SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview);
        sharedPreference = new SharedPreference(this);
        context=this;
        address = sharedPreference.GetAlarmAddressData();
        title = sharedPreference.GetAlarmTitleData();
        todo = new ArrayList<String>();

        if(title.size()==0){
            CreateAlertForNoAlarms();
        }

        else {

            listView = (ListView) findViewById(R.id.myList);

            /****** Initializing my Adapter ******/

            myAdapter = new MyAdapter(this, title, todo, address, 0);

            listView.setAdapter(myAdapter);
        }
    }

    public void CreateAlertForNoAlarms(){
        new AlertDialog.Builder(context)
                .setTitle("No Alarms found!!")
                .setMessage("Do you want to set an alarm right now?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context,MainAct.class);
                        context.startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
