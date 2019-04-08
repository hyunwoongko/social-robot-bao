package com.welfarerobotics.welfareapplcation.ui.initial;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import com.google.firebase.database.*;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.model.UserModel;
import com.welfarerobotics.welfareapplcation.model.UserSingleton;
import com.welfarerobotics.welfareapplcation.ui.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.ui.main.MainActivity;
import com.welfarerobotics.welfareapplcation.ui.menu.ConversationEdit;
import android.content.SharedPreferences;
import com.welfarerobotics.welfareapplcation.util.Preference;
import com.welfarerobotics.welfareapplcation.util.UUID;

public class SplashActivity extends BaseActivity {

    private static final String TODO = null;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        boolean isFirstUser = Preference.get(this)
                .getBoolean("firstUser", true);

        if (isFirstUser) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, TutorialActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);// 스택에싸여있는 Activity 삭제
                finish();
            }, 3000);
        } else {
            FirebaseDatabase.getInstance()
                    .getReference("user")
                    .child(UUID.getId(this))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserModel model = dataSnapshot.getValue(UserModel.class);
                            UserSingleton.setInstance(model);

                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);// 스택에싸여있는 Activity 삭제
                                finish();
                            }, 1000);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }
//        try {
//            sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}