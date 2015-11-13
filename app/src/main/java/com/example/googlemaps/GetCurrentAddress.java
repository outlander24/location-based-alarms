package com.example.googlemaps;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

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

/**
 * Created by ashishtotla on 30-03-2015.
 */
public class GetCurrentAddress extends AsyncTask<Location, Void, String>{
    Context mContext;
    private onLocationFetchedListener mOnLocationFetchedListener;

    public GetCurrentAddress(Context context, onLocationFetchedListener onLocationFetchedListener){
        mContext = context;
        mOnLocationFetchedListener = onLocationFetchedListener;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Location... strings) {

        String uri = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + strings[0].getLatitude()+","+strings[0].getLongitude() + "&sensor=true";
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
            String add = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getString("formatted_address");
            //        add = url[0];
            return add;

        } catch (JSONException e) {

            return "Cannot found your location";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        mOnLocationFetchedListener.onLocationFetched(result);
    }

    public interface onLocationFetchedListener {
        void onLocationFetched(String result);
    }
}
