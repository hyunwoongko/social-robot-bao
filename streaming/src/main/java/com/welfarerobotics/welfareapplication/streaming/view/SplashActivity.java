package com.welfarerobotics.welfareapplication.streaming.view;

import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.welfarerobotics.welfareapplication.streaming.R;
import com.welfarerobotics.welfareapplication.streaming.base.BaseActivity;
import com.welfarerobotics.welfareapplication.streaming.databinding.SplashView;
import com.welfarerobotics.welfareapplication.streaming.viewmodel.SplashViewModel;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 12:25
 * @homepage : https://github.com/gusdnd852
 */
public class SplashActivity extends BaseActivity<SplashView, SplashViewModel> {

    @Override protected int bindView() {
        return R.layout.splash_view;
    }

    @Override protected void observe() {
        viewModel.getSplashEvent()
                .observe(this, o -> startActivity(SignInActivity.class));
    }
}
