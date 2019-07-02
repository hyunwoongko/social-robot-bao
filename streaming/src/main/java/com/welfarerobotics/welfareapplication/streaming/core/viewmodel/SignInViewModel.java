package com.welfarerobotics.welfareapplication.streaming.core.viewmodel;

import android.app.Application;
import android.support.annotation.NonNull;
import com.welfarerobotics.welfareapplication.streaming.base.BaseViewModel;
import com.welfarerobotics.welfareapplication.streaming.core.model.SignInModel;
import lombok.Getter;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 1:11
 * @homepage : https://github.com/gusdnd852
 */

@Getter
public class SignInViewModel extends BaseViewModel<SignInModel> {

    public SignInViewModel(@NonNull Application application) {
        super(application);
    }

    @Override public SignInModel onCreate() {
        return super.onCreate();
    }
}
