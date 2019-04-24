package com.welfarerobotics.welfareapplcation.core.splash;

import android.content.Intent;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.greeting.GreetingActivity;
import com.welfarerobotics.welfareapplcation.core.main.MainActivity;
import com.welfarerobotics.welfareapplcation.databinding.SplashView;
import com.welfarerobotics.welfareapplcation.util.Preference;
import com.welfarerobotics.welfareapplcation.util.UUID;

public class SplashActivity extends BaseActivity<SplashView, SplashViewModel> {

    @Override
    public void onCreateView(int layout, Class clazz) {
        super.onCreateView(R.layout.splash_view, SplashViewModel.class);
    }

    @Override public void observeData() {
        viewModel.getUuidData().setValue(UUID.getId(this));
        viewModel.getIsFirstUser().setValue(Preference.get(this).getBoolean("firstUser", true));
    }

    @Override public void observeAction() {
        viewModel.getMoveToTutorialActivityEvent().observe(this, o -> {
            Intent intent = new Intent(SplashActivity.this, GreetingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        viewModel.getMoveToMainActivityEvent().observe(this, o -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}