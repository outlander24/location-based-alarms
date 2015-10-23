package com.example.googlemaps;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ashishtotla on 02-04-2015.
 */
public class MyAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declaring Used Variables *********/

    private Activity activity;
    private ArrayList title;
    private ArrayList todo;
    private ArrayList address;
    private static LayoutInflater inflater=null;

    /*************  CustomAdapter Constructor *****************/
    public MyAdapter(Activity a, ArrayList title, ArrayList todo, ArrayList address,int activityflag) {

        activity = a;
        this.title = title;
        this.todo = todo;
        this.address = address;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void UpdateData(ArrayList<String> title, ArrayList<String> address){
        this.title = title;
        this.address = address;
        notifyDataSetChanged();
    }

    /******** Size of the passed Arraylist ************/
    public int getCount() {
        if(title.size()==0){
            return 1;
        }
        return title.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView Title;
        public TextView ToDo;
        public TextView Address;
        public RelativeLayout menuOptions;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflating players_list xml for every row element *******/
            vi = inflater.inflate(R.layout.my_card, null);

            /****** View Holder Object to contain player_list.xml file elements ******/

            holder = new ViewHolder();
            holder.Title = (TextView) vi.findViewById(R.id.title);
            holder.ToDo=(TextView)vi.findViewById(R.id.todo);
            holder.Address= (TextView) vi.findViewById(R.id.address);

            Typeface typeface = Typeface.createFromAsset(activity.getAssets(),"Aaargh.ttf");

            holder.Title.setTypeface(typeface);
            holder.Address.setTypeface(typeface);
            holder.menuOptions = (RelativeLayout)vi.findViewById(R.id.menuButtons);

            vi.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreference sharedPreference = new SharedPreference(activity);
                    sharedPreference.RemoveAlarmData(position);
                    UpdateData(sharedPreference.GetAlarmTitleData(), sharedPreference.GetAlarmAddressData());
                }
            });

            /************  Setting holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(title.size()<=0)
        {
            holder.Title.setText("Create a To-Do");
            holder.Address.setText("");
            holder.menuOptions.setVisibility(View.INVISIBLE);

        }
        else
        {

            /************  Setting Model values in Holder elements ***********/
            holder.menuOptions.setVisibility(View.VISIBLE);
            holder.Title.setText(""+title.get(position));
            holder.Address.setText(""+address.get(position));

            /******** Setting Item Click Listner for LayoutInflater for each row *******/

//            vi.setOnClickListener(new OnItemClickListener(position, vi));
            Animation animation = AnimationUtils.loadAnimation(activity,R.anim.myanimation);
            animation.setRepeatCount(Animation.INFINITE);
            vi.startAnimation(animation);
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    /********* Called when Item is clicked in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
//        private int mPosition;
//        private View view;
//
//        OnItemClickListener(int position, View v){
//            mPosition = position;
//            view = v;
//        }
//
        @Override
        public void onClick(View arg0) {

//            if(view.findViewById(R.id.menuButtons).getVisibility()== RelativeLayout.GONE) {
//                view.findViewById(R.id.menuButtons).setVisibility(RelativeLayout.VISIBLE);
//            }
//            else{
//                view.findViewById(R.id.menuButtons).setVisibility(RelativeLayout.GONE);
//            }
//
        }
    }
}