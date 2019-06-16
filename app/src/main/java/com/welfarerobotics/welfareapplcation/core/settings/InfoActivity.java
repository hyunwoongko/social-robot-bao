package com.welfarerobotics.welfareapplcation.core.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.util.Sound;

/**
 * @author : Hyunwoong
 * @when : 6/16/2019 12:10 PM
 * @homepage : https://github.com/gusdnd852
 */
public class InfoActivity extends BaseActivity {
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    @Override protected void onResume() {
        super.onResume();
        Sound.get().resume(this, R.raw.dev);
        Sound.get().loop(true);
    }

    @Override protected void onPause() {
        super.onPause();
        Sound.get().pause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        Sound.get().stop();
    }
}
