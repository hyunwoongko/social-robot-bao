package com.welfarerobotics.welfareapplcation.api.chat.tools;

import android.app.Activity;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.util.FirebaseHelper;
import com.welfarerobotics.welfareapplcation.util.NetworkUtil;
import es.dmoral.toasty.Toasty;

import java.util.concurrent.TimeUnit;

/**
 * @Author : Hyunwoong
 * @When : 5/13/2019 8:38 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class Pituitary { // 호르몬 분비 클래스
    public static void secrete(Activity activity) {
        NetworkUtil.wifiSafe(activity, () -> {
            try {
                FirebaseHelper.get().connect(FirebaseDatabase.getInstance()
                        .getReference("chat")
                        .child("open_domain")
                        .child("answer"), dataSnapshot -> {
                    Iterable<DataSnapshot> list = dataSnapshot.getChildren();

                    for (Object o : list) {
                        Toast.makeText(activity, o.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                });
            } catch (Throwable e) {
                System.out.println("아직 데이터가 만들어지지 않았음 !!!!");
            }
        });
    }
}
