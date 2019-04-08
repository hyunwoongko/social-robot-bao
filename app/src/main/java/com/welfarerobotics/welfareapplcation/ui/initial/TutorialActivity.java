package com.welfarerobotics.welfareapplcation.ui.initial;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import com.welfarerobotics.welfareapplcation.ui.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.R;

import static com.welfarerobotics.welfareapplcation.ui.initial.BlActivity.ACCESS_COARSE_LOCATION_CODE;
import static com.welfarerobotics.welfareapplcation.ui.initial.BlActivity.ACCESS_FINE_LOCATION_CODE;

public class TutorialActivity extends BaseActivity {
    private ImageView startimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_tutorial);

        startimage = findViewById(R.id.start_imageView);
        startimage.setOnClickListener(view -> {
            Intent intent = new Intent(this, BlActivity.class);
            startActivity(intent);
            finish();
        });

        //권한 확인
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_ADMIN}, 1);

    }
}
