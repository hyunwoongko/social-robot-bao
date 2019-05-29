package com.welfarerobotics.welfareapplcation.core.main;

import android.media.AudioManager;
import android.os.Bundle;
import android.view.MotionEvent;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.bot.ear.EarSet;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.entity.Server;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import com.welfarerobotics.welfareapplcation.util.data_loader.DataLoader;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;
import com.welfarerobotics.welfareapplcation.util.touch_util.ConcreteSwipeTouchListener;
import com.welfarerobotics.welfareapplcation.util.touch_util.OnSwipeTouchListener;

public class MainActivity extends BaseActivity {

    private OnSwipeTouchListener onSwipeTouchListener;
    private AudioManager audioManager;
    private EarSet ear = new EarSet(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        onSwipeTouchListener = new ConcreteSwipeTouchListener(this, audioManager);

        FirebaseHelper.get().connect(FirebaseDatabase
                .getInstance()
                .getReference("server"), dataSnapshot -> {
            Server server = dataSnapshot.getValue(Server.class);
            System.out.println(server);
            ServerCache.setInstance(server);
            DataLoader.onDataLoad(); // 모든 데이터 다운로드
        });
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
        onSwipeTouchListener.getGestureDetector().onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }
}