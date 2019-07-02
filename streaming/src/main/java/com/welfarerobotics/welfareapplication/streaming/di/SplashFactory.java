package com.welfarerobotics.welfareapplication.streaming.di;

import android.app.Application;
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

        private Application application;

        public SplashModule(Application application) {
            this.application = application;
        }

        @Provides
        public SplashViewModel provideViewModel() {
            return ViewModelProviders.getInstance(application).create(SplashViewModel.class);
        }
    }
}

