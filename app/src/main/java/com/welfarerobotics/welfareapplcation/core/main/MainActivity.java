package com.welfarerobotics.welfareapplcation.core.main;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.bot.ear.EarSet;
import com.welfarerobotics.welfareapplcation.bot.face.Detect;
import com.welfarerobotics.welfareapplcation.bot.face.FaceHandler;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.initial.SplashActivity;
import com.welfarerobotics.welfareapplcation.entity.Server;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import com.welfarerobotics.welfareapplcation.util.bluetooth.Bluetooth;
import com.welfarerobotics.welfareapplcation.util.data_loader.DataLoader;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;
import com.welfarerobotics.welfareapplcation.util.touch_util.ConcreteSwipeTouchListener;
import com.welfarerobotics.welfareapplcation.util.touch_util.OnSwipeTouchListener;

public class MainActivity extends BaseActivity {

    private OnSwipeTouchListener onSwipeTouchListener;
    private AudioManager audioManager;
    private EarSet ear = new EarSet(this);
    private ImageView refresh_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        refresh_view = (ImageView) findViewById(R.id.refresh);
        refresh_view.setOnClickListener(view -> {
            finish();
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
        });
        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        FirebaseHelper.get().connect(FirebaseDatabase
                .getInstance()
                .getReference("server"), dataSnapshot -> {
            Server server = dataSnapshot.getValue(Server.class);
            System.out.println(server);
            ServerCache.setInstance(server);
            DataLoader.onDataLoad(); // 모든 데이터 다운로드
            ear.initEar();
            onSwipeTouchListener = new ConcreteSwipeTouchListener(this, audioManager, ear::repeat);
        });


        ImageView eyes = (ImageView)findViewById(R.id.eye);
        ImageView mouse =(ImageView)findViewById(R.id.s_mouse);
        TextView emotion = (TextView)findViewById(R.id.emotion);
        Bluetooth bluetooth =Bluetooth.getInstance(this);
        //FaceHandler handler = new FaceHandler(eyes);
        Handler handler = new FaceHandler(eyes,emotion,mouse);
        new Detect(handler);

    }

    @Override
    protected void onResume() {
        super.onResume();
        audioManager.setMicrophoneMute(false);
        ear.startHear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        audioManager.setMicrophoneMute(true);
        ear.blockHear();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(onSwipeTouchListener != null){
            onSwipeTouchListener.getGestureDetector().onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
}