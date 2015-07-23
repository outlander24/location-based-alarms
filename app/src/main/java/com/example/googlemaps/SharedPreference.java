package com.example.googlemaps;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ashishtotla on 03-04-2015.
 */
public class SharedPreference {

    private Context context;
    SharedPreferences sharedPreferences;

    public SharedPreference(Context context){
        this.context =context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void SaveAlarmData(ArrayList<String> title, ArrayList<String> address, ArrayList<LatLng> latLng, ArrayList<Float> range){
        Gson gson = new Gson();

        sharedPreferences.edit().putString("TITLE",gson.toJson(title)).commit();
        sharedPreferences.edit().putString("ADDRESS",gson.toJson(address)).commit();
        sharedPreferences.edit().putString("LATLNG",gson.toJson(latLng)).commit();
        sharedPreferences.edit().putString("RANGE",gson.toJson(range)).commit();
    }

    public void AddAlarmData(String title, String address, LatLng latLng, float range){
        ArrayList<String> titleList = GetAlarmTitleData();
        ArrayList<String> addressList = GetAlarmAddressData();
        ArrayList<LatLng> latLngList = GetAlarmLatLngData();
        ArrayList<Float> rangeList = GetAlarmRangeData();

        titleList.add(title);
        addressList.add(address);
        latLngList.add(latLng);
        rangeList.add(range);

        SaveAlarmData(titleList, addressList, latLngList, rangeList);
    }

    public void RemoveAlarmData(int position){
        ArrayList<String> titleList = GetAlarmTitleData();
        ArrayList<String> addressList = GetAlarmAddressData();
        ArrayList<LatLng> latLngList = GetAlarmLatLngData();
        ArrayList<Float> rangeList = GetAlarmRangeData();

        if(titleList.size()!=0){
            titleList.remove(position);
            addressList.remove(position);
            latLngList.remove(position);
            rangeList.remove(position);
            SaveAlarmData(titleList, addressList, latLngList, rangeList);
        }
    }

    public ArrayList<String> GetAlarmTitleData(){
        List<String> titleList;
        String jsonTitle = sharedPreferences.getString("TITLE",null);
        if(jsonTitle != null){
            Gson gson = new Gson();
            String[] title = gson.fromJson(jsonTitle,String[].class);
            titleList = Arrays.asList(title);
            titleList = new ArrayList<String>(titleList);
        }
        else{
            return new ArrayList<String>();
        }

        return (ArrayList<String>) titleList;
    }

    public ArrayList<String> GetAlarmAddressData(){
        List<String> addressList;
        String jsonAddress = sharedPreferences.getString("ADDRESS",null);
        if(jsonAddress != null){
            Gson gson = new Gson();
            String[] address = gson.fromJson(jsonAddress,String[].class);
            addressList = Arrays.asList(address);
            addressList = new ArrayList<String>(addressList);
        }
        else{
            return new ArrayList<String>();
        }

        return (ArrayList<String>) addressList;
    }

    public ArrayList<LatLng> GetAlarmLatLngData(){
        List<LatLng> latLngList;
        String jsonLatLng = sharedPreferences.getString("LATLNG",null);
        if(jsonLatLng != null){
            Gson gson = new Gson();
            LatLng[] latLng = gson.fromJson(jsonLatLng,LatLng[].class);
            latLngList = Arrays.asList(latLng);
            latLngList = new ArrayList<LatLng>(latLngList);
        }
        else{
            return new ArrayList<LatLng>();
        }

        return (ArrayList<LatLng>) latLngList;
    }

    public ArrayList<Float> GetAlarmRangeData(){
        List<Float> rangeList;
        String jsonRange = sharedPreferences.getString("RANGE",null);
        if(jsonRange != null){
            Gson gson = new Gson();
            Float[] range = gson.fromJson(jsonRange,Float[].class);
            rangeList = Arrays.asList(range);
            rangeList = new ArrayList<Float>(rangeList);
        }
        else{
            return new ArrayList<Float>();
        }

        return (ArrayList<Float>) rangeList;
    }


}
