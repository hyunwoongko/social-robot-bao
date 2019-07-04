package com.welfarerobotics.welfareapplication.streaming.core.view;

import android.view.animation.AnimationUtils;
import com.welfarerobotics.welfareapplication.streaming.R;
import com.welfarerobotics.welfareapplication.streaming.base.BaseActivity;
import com.welfarerobotics.welfareapplication.streaming.core.viewmodel.SplashViewModel;
import com.welfarerobotics.welfareapplication.streaming.databinding.SplashView;
import com.welfarerobotics.welfareapplication.streaming.di.DaggerSplashFactory;
import com.welfarerobotics.welfareapplication.streaming.di.SplashFactory.SplashModule;
import com.welfarerobotics.welfareapplication.streaming.util.sound.Sound;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 12:25
 * @homepage : https://github.com/gusdnd852
 */
public class SplashActivity extends BaseActivity<SplashView, SplashViewModel> {

    @Override protected int injectView() {
        DaggerSplashFactory.builder()
                .splashModule(new SplashModule())
                .build().inject(this);

        return R.layout.splash_view;
    }

    @Override protected void observe() {
        viewModel.getPlaySound().observe(this,
                o -> Sound.get().effectSound(this, R.raw.intro));

        viewModel.getMoveActivity().observe(this,
                o -> startActivityAndFinish(SignInActivity.class));

        view.splashLogo.startAnimation(AnimationUtils
                .loadAnimation(this, R.anim.logo_fade_in));
    }
}
