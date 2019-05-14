package com.welfarerobotics.welfareapplcation.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.kinda.alert.KAlertDialog;
import com.welfarerobotics.welfareapplcation.core.initial.InitialWifiActivity;

/**
 * @Author : Hyunwoong
 * @When : 5/12/2019 10:07 AM
 * @Homepage : https://github.com/gusdnd852
 */
public class NetworkUtil {
    public static boolean isOnline(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void wifiSafe(Activity activity) {
        if (!isOnline(activity)) {
            KAlertDialog pDialog = new KAlertDialog(activity);
            pDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("네트워크 연결 에러");
            pDialog.setContentText("와이파이 네트워크에 연결해야\n원활한 사용이 가능합니다.");
            pDialog.setCancelable(false);
            pDialog.setConfirmText("확인");
            pDialog.setConfirmClickListener(kAlertDialog -> activity.startActivity(new Intent(activity, InitialWifiActivity.class)));
            pDialog.show();
        }
    }

    public static void wifiSafe(Activity activity, Runnable runnable) {
        if (isOnline(activity)) {
            runnable.run();
        } else {
            KAlertDialog pDialog = new KAlertDialog(activity);
            pDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("네트워크 연결 에러");
            pDialog.setContentText("와이파이 네트워크에 연결해야\n원활한 사용이 가능합니다.");
            pDialog.setCancelable(false);
            pDialog.setConfirmText("확인");
            pDialog.setConfirmClickListener(kAlertDialog -> activity.startActivity(new Intent(activity, InitialWifiActivity.class)));
            pDialog.show();
        }
    }
}
