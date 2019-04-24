package com.welfarerobotics.welfareapplcation.core.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * @Author : Hyunwoong
 * @When : 4/24/2019 12:26 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class BaseViewModel extends AndroidViewModel {
    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public Context getContext() {
        return getApplication().getApplicationContext();
    }

    public void onCreate() {

    }

    @Override protected void onCleared() {
        super.onCleared();
    }

}
