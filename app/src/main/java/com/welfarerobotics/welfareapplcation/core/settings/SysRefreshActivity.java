package com.welfarerobotics.welfareapplcation.core.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import com.google.firebase.database.*;
import com.kinda.alert.KAlertDialog;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.entity.Quiz;
import com.welfarerobotics.welfareapplcation.entity.User;
import com.welfarerobotics.welfareapplcation.entity.cache.UserCache;
import com.welfarerobotics.welfareapplcation.util.data_util.Preference;

import java.util.Random;

public class SysRefreshActivity extends BaseActivity {
    private Toolbar toolbar;
    private ImageView baoimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initconversationmenu);
        toolbar = findViewById(R.id.con_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("시스템 초기화");
        ImageView baoimg = (ImageView) findViewById(R.id.bao_imageview);
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
