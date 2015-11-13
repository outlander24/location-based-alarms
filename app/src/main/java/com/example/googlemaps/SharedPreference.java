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

    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public SharedPreference(Context context){
        this.mContext =context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void saveAlarmData(ArrayList<String> title, ArrayList<String> address, ArrayList<LatLng> latLng, ArrayList<Float> range){
        Gson gson = new Gson();

        mSharedPreferences.edit().putString("TITLE",gson.toJson(title)).apply();
        mSharedPreferences.edit().putString("ADDRESS",gson.toJson(address)).apply();
        mSharedPreferences.edit().putString("LATLNG",gson.toJson(latLng)).apply();
        mSharedPreferences.edit().putString("RANGE",gson.toJson(range)).apply();
    }

    public void addAlarmData(String title, String address, LatLng latLng, float range){
        ArrayList<String> titleList = getAlarmTitleData();
        ArrayList<String> addressList = getAlarmAddressData();
        ArrayList<LatLng> latLngList = getAlarmLatLngData();
        ArrayList<Float> rangeList = getAlarmRangeData();

        titleList.add(title);
        addressList.add(address);
        latLngList.add(latLng);
        rangeList.add(range);

        saveAlarmData(titleList, addressList, latLngList, rangeList);
    }

    public void removeAlarmData(int position){
        ArrayList<String> titleList = getAlarmTitleData();
        ArrayList<String> addressList = getAlarmAddressData();
        ArrayList<LatLng> latLngList = getAlarmLatLngData();
        ArrayList<Float> rangeList = getAlarmRangeData();

        if(titleList.size()!=0){
            titleList.remove(position);
            addressList.remove(position);
            latLngList.remove(position);
            rangeList.remove(position);
            saveAlarmData(titleList, addressList, latLngList, rangeList);
        }
    }

    public void removeAlarmData(String title) {
        ArrayList<String> titleList = getAlarmTitleData();
        ArrayList<String> addressList = getAlarmAddressData();
        ArrayList<LatLng> latLngList = getAlarmLatLngData();
        ArrayList<Float> rangeList = getAlarmRangeData();

        int position = titleList.indexOf(title);

        if(titleList.size()!=0){
            titleList.remove(position);
            addressList.remove(position);
            latLngList.remove(position);
            rangeList.remove(position);
            saveAlarmData(titleList, addressList, latLngList, rangeList);
        }
    }

    public ArrayList<String> getAlarmTitleData(){
        List<String> titleList;
        String jsonTitle = mSharedPreferences.getString("TITLE",null);
        if(jsonTitle != null){
            Gson gson = new Gson();
            String[] title = gson.fromJson(jsonTitle,String[].class);
            titleList = Arrays.asList(title);
            titleList = new ArrayList<>(titleList);
        }
        else{
            return new ArrayList<>();
        }

        return (ArrayList<String>) titleList;
    }

    public ArrayList<String> getAlarmAddressData(){
        List<String> addressList;
        String jsonAddress = mSharedPreferences.getString("ADDRESS",null);
        if(jsonAddress != null){
            Gson gson = new Gson();
            String[] address = gson.fromJson(jsonAddress,String[].class);
            addressList = Arrays.asList(address);
            addressList = new ArrayList<>(addressList);
        }
        else{
            return new ArrayList<>();
        }

        return (ArrayList<String>) addressList;
    }

    public ArrayList<LatLng> getAlarmLatLngData(){
        List<LatLng> latLngList;
        String jsonLatLng = mSharedPreferences.getString("LATLNG",null);
        if(jsonLatLng != null){
            Gson gson = new Gson();
            LatLng[] latLng = gson.fromJson(jsonLatLng,LatLng[].class);
            latLngList = Arrays.asList(latLng);
            latLngList = new ArrayList<>(latLngList);
        }
        else{
            return new ArrayList<>();
        }

        return (ArrayList<LatLng>) latLngList;
    }

    public ArrayList<Float> getAlarmRangeData(){
        List<Float> rangeList;
        String jsonRange = mSharedPreferences.getString("RANGE",null);
        if(jsonRange != null){
            Gson gson = new Gson();
            Float[] range = gson.fromJson(jsonRange,Float[].class);
            rangeList = Arrays.asList(range);
            rangeList = new ArrayList<>(rangeList);
        }
        else{
            return new ArrayList<>();
        }

        return (ArrayList<Float>) rangeList;
    }


}
