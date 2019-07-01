package com.welfarerobotics.welfareapplcation.core.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.util.ToastType;
import es.dmoral.toasty.Toasty;

/**
 * @Author : Hyunwoong
 * @When : 5/6/2019 12:01 PM
 * @Homepage : https://github.com/gusdnd852
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //화면 항상 켜짐
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    @Override public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    public void showToast(String msg, ToastType type) {
        if (type == ToastType.error) {
            Toasty.error(this, msg, Toast.LENGTH_SHORT).show();
        } else if (type == ToastType.normal) {
            Toasty.normal(this, msg, Toast.LENGTH_SHORT).show();
        } else if (type == ToastType.info) {
            Toasty.info(this, msg, Toast.LENGTH_SHORT).show();
        } else if (type == ToastType.success) {
            Toasty.success(this, msg, Toast.LENGTH_SHORT).show();
        } else if (type == ToastType.warning) {
            Toasty.warning(this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
