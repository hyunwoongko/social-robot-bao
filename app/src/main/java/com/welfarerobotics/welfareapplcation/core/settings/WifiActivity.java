package com.welfarerobotics.welfareapplcation.core.settings;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;

public class WifiActivity extends BaseActivity {
    private FragmentManager fm;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        toolbar = findViewById(R.id.wifiToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Wi-Fi 설정");

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        WifiFragment wifiFragment = WifiFragment.newInstance();
        ft.replace(R.id.main_container, wifiFragment);
        ft.commit();
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