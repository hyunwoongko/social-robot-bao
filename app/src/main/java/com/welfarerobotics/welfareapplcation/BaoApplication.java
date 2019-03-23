package com.welfarerobotics.welfareapplcation;

import android.app.Application;
import com.welfarerobotics.welfareapplcation.chat_api.ModelApi;

/**
 * @Author : Hyunwoong
 * @When : 3/23/2019 4:55 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class BaoApplication extends Application {

    private static ModelApi modelApi;

    @Override public void onCreate() {
        super.onCreate();
        modelApi = new ModelApi();

    }

    public static ModelApi getModelApi() {
        return modelApi;
    }
}
