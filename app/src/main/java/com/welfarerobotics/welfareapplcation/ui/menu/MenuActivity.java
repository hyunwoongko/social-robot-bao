package com.welfarerobotics.welfareapplcation.ui.menu;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.ui.settings.SettingActivity;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ImageButton ibSettings = findViewById(R.id.settings);
        ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        ibSettings.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
        });
    }
}
