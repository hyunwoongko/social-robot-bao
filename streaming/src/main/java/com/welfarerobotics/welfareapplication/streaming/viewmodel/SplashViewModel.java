package com.welfarerobotics.welfareapplication.streaming.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import com.welfarerobotics.welfareapplication.streaming.base.BaseViewModel;
import com.welfarerobotics.welfareapplication.streaming.util.LiveData;
import lombok.Data;
import lombok.Getter;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 12:36
 * @homepage : https://github.com/gusdnd852
 */
@Getter
public class SplashViewModel extends BaseViewModel {

    private LiveData splashEvent = new LiveData();

    public SplashViewModel(@NonNull Application application) {
        super(application);
    }
}
