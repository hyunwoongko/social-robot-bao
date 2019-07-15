package com.welfarerobotics.welfareapplcation.core.initial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyPresigningClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.main.MainActivity;
import com.welfarerobotics.welfareapplcation.entity.Server;
import com.welfarerobotics.welfareapplcation.entity.User;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import com.welfarerobotics.welfareapplcation.entity.cache.UserCache;
import com.welfarerobotics.welfareapplcation.util.DeviceId;
import com.welfarerobotics.welfareapplcation.util.NetworkUtil;
import com.welfarerobotics.welfareapplcation.util.Sound;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;
import com.welfarerobotics.welfareapplcation.util.data_util.Preference;

import java8.util.function.Consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends BaseActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Sound.get().start(this, R.raw.intro); // 효과음 재생
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_fade_in);
        findViewById(R.id.welfare_logo).startAnimation(fadeInAnimation); // 애니메이션 재생
        boolean firstUser = Preference.get(this).getBoolean("isFirst", true);


        Handler handler = new Handler();
        if (firstUser) {
            handler.postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, GreetingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }, 3000);
        } else {
            NetworkUtil.wifiSafe(this, () -> FirebaseHelper.get().connect(FirebaseDatabase
                    .getInstance()
                    .getReference("server"), snapshot -> {

                Server server = snapshot.getValue(Server.class);
                ServerCache.setInstance(server);

                FirebaseHelper.get().connect(FirebaseDatabase
                        .getInstance()
                        .getReference("user")
                        .child(DeviceId.getInstance(getApplicationContext()).getUUID()), dataSnapshots -> {

                    User user = dataSnapshots.getValue(User.class);
                    UserCache.setInstance(user);
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }, 2000);
                });
            }));
        }
    }
}