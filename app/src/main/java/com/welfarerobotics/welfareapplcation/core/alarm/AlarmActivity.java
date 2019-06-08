package com.welfarerobotics.welfareapplcation.core.alarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import com.welfarerobotics.welfareapplcation.R;

public class AlarmActivity extends AbstractAlarmActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
    }
}
