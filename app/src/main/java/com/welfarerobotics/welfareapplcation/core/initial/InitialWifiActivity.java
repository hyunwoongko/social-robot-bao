package com.welfarerobotics.welfareapplcation.core.initial;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.settings.WifiFragment;
import com.welfarerobotics.welfareapplcation.util.Sound;

public class InitialWifiActivity extends BaseActivity {
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_wifi);

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        WifiFragment wifiFragment = WifiFragment.newInstance();
        ft.replace(R.id.main_container, wifiFragment);
        ft.commit();

        findViewById(R.id.next_imageView).setOnClickListener(v -> {
            Sound.get().effectSound(getApplicationContext(), R.raw.click);
            SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
            if (pref.getBoolean("isFirst", true)) {
                startActivity(new Intent(InitialWifiActivity.this, InitialSettingActivity.class));
            } else {
                startActivity(new Intent(InitialWifiActivity.this, SplashActivity.class));
            }
            finish();
        });
    }
}