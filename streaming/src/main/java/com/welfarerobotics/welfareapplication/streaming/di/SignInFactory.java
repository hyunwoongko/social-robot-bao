package com.welfarerobotics.welfareapplication.streaming.di;

import android.app.Application;
import com.welfarerobotics.welfareapplication.streaming.core.view.SignInActivity;
import com.welfarerobotics.welfareapplication.streaming.core.viewmodel.SignInViewModel;
import dagger.Component;
import dagger.Module;
import dagger.Provides;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 6:26
 * @homepage : https://github.com/gusdnd852
 */
@Component(modules = SignInFactory.SignInModule.class)
public interface SignInFactory {
    void inject(SignInActivity activity);

    @Module class SignInModule {

        @Provides public SignInViewModel provideViewModel() {
            return ViewModelFactory.getInstance().create(SignInViewModel.class);
        }
    }
}


