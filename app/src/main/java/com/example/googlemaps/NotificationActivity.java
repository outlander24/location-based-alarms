package com.example.googlemaps;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ashish on 12/11/15.
 */
public class NotificationActivity extends Activity {

    private TextView mTitleTv;
    private Button mBtnDone;
    SharedPreference sharedPreference;
    private String mTitleText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.notification_layout);
        sharedPreference = new SharedPreference(this);
        mTitleText = getIntent().getExtras().getString("TITLE");
        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mBtnDone = (Button) findViewById(R.id.done);
        mTitleTv.setText(mTitleText);

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreference.removeAlarmData(mTitleText);
                finish();
            }
        });
    }
}
