package com.welfarerobotics.welfareapplcation.core.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.firebase.database.*;
import com.kinda.alert.KAlertDialog;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.entity.Quiz;
import com.welfarerobotics.welfareapplcation.entity.User;
import com.welfarerobotics.welfareapplcation.entity.cache.UserCache;
import com.welfarerobotics.welfareapplcation.util.DeviceId;
import com.welfarerobotics.welfareapplcation.util.ToastType;
import com.welfarerobotics.welfareapplcation.util.data_util.Preference;

import java.util.Random;

public class SysRefreshActivity extends BaseActivity {
    private Toolbar toolbar;
    private ImageView baoimg;
    private Button changebtn;
    private  ImageView backimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_refresh);
//        toolbar = findViewById(R.id.con_menu);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("시스템 초기화");
        backimg = (ImageView) findViewById(R.id.backview);
        backimg.setOnClickListener(view -> {
            finish();
        });

        baoimg = (ImageView) findViewById(R.id.Bao_view);
        baoimg.setOnClickListener(view -> {
            startActivity(new Intent(this, SysRfPopupActivity.class));
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
