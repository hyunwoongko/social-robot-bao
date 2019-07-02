package com.welfarerobotics.welfareapplication.streaming.core.viewmodel;

import android.app.Application;
import android.support.annotation.NonNull;
import com.welfarerobotics.welfareapplication.streaming.base.BaseViewModel;
import com.welfarerobotics.welfareapplication.streaming.base.marker.Model;
import com.welfarerobotics.welfareapplication.streaming.core.model.SplashModel;
import com.welfarerobotics.welfareapplication.streaming.util.LiveData;
import lombok.Getter;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 12:36
 * @homepage : https://github.com/gusdnd852
 */

@Getter
public class SplashViewModel extends BaseViewModel<SplashModel> {

    public SplashViewModel(@NonNull Application application) {
        super(application);
    }

    @Override public SplashModel onCreate() {
        return super.onCreate();
    }

    public void splashEvent() {

    }
}
