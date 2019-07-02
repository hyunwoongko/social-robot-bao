package com.welfarerobotics.welfareapplication.streaming.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import com.welfarerobotics.welfareapplication.streaming.base.BaseViewModel;
import lombok.Getter;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 1:11
 * @homepage : https://github.com/gusdnd852
 */

@Getter
public class SignInViewModel extends BaseViewModel {

    public SignInViewModel(@NonNull Application application) {
        super(application);
    }
}
