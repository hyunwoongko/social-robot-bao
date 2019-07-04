package com.welfarerobotics.welfareapplication.streaming.di;

import com.welfarerobotics.welfareapplication.streaming.core.view.SplashActivity;
import com.welfarerobotics.welfareapplication.streaming.core.viewmodel.SplashViewModel;
import dagger.Component;
import dagger.Module;
import dagger.Provides;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 6:14
 * @homepage : https://github.com/gusdnd852
 */

@Component(modules = SplashFactory.SplashModule.class)
public interface SplashFactory {
    void inject(SplashActivity activity);

    @Module class SplashModule {

        @Provides public SplashViewModel provideViewModel() {
            return ViewModelFactory.getInstance().create(SplashViewModel.class);
        }
    }
}

