package com.welfarerobotics.welfareapplication.streaming.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import com.welfarerobotics.welfareapplication.streaming.base.marker.Model;
import lombok.Getter;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 1:16
 * @homepage : https://github.com/gusdnd852
 * <p>
 * Template Method 패턴으로 구현하기
 */
@Getter
public abstract class BaseViewModel<M extends Model> extends AndroidViewModel {

    private M model;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        model = onCreate();
    }

    public M onCreate() {
        return null;
    }

    public Context getContext() {
        return getApplication().getApplicationContext();
    }
}
