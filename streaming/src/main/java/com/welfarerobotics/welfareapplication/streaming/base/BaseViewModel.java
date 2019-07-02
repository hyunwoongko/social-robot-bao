package com.welfarerobotics.welfareapplication.streaming.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 1:16
 * @homepage : https://github.com/gusdnd852
 * <p>
 * Template Method 패턴으로 구현하기
 */
public abstract class BaseViewModel extends AndroidViewModel {

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }
}
