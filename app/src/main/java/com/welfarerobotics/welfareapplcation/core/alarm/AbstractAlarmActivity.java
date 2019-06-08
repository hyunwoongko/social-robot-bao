package com.welfarerobotics.welfareapplcation.core.alarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import com.welfarerobotics.welfareapplcation.R;

/**
 * @author : Hyunwoong
 * @when : 6/8/2019 4:20 AM
 * @homepage : https://github.com/gusdnd852
 */
public abstract class AbstractAlarmActivity extends AppCompatActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.SwitchTheme);
    }
}
