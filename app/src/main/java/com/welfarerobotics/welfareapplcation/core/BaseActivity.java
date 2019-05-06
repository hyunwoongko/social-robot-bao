package com.welfarerobotics.welfareapplcation.core;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
