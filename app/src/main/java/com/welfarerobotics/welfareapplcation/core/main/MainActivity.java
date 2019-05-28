package com.welfarerobotics.welfareapplcation.core.main;

import android.media.AudioManager;
import android.os.Bundle;
import android.view.MotionEvent;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.bot.Ear;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.entity.Server;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import com.welfarerobotics.welfareapplcation.util.Pool;
import com.welfarerobotics.welfareapplcation.util.ToastType;
import com.welfarerobotics.welfareapplcation.util.data_loader.DataLoader;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;
import com.welfarerobotics.welfareapplcation.util.touch_util.ConcreteSwipeTouchListener;
import com.welfarerobotics.welfareapplcation.util.touch_util.OnSwipeTouchListener;

public class MainActivity extends BaseActivity {

    private OnSwipeTouchListener onSwipeTouchListener;
    private AudioManager audioManager;
    private SpeechRecognizerClient ear; // 청각

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        onSwipeTouchListener = new ConcreteSwipeTouchListener(this, audioManager);
        ear = new SpeechRecognizerClient.Builder().setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION).build();

        FirebaseHelper.get().connect(FirebaseDatabase
                .getInstance()
                .getReference("server"), dataSnapshot -> {
            Server server = dataSnapshot.getValue(Server.class);
            System.out.println(server);
            ServerCache.setInstance(server);
            init(); // 서버 로드 후 초기화 진행
        });
    }

    private void init() {
        DataLoader.onDataLoad(); // 모든 데이터 다운로드
        Ear hearing = new Ear();// 들었을 때 행동 설정

        hearing.onSuccess(speech -> Pool.threadPool.execute(() -> {
            if (hearing.isNameDetected(speech)) {

                Brain.think(speech); // 생각
                Brain.speech(Mouth.get()); // 말하기
                Mouth.get().stop(() -> ear.startRecording(false)); // 다시 듣기
            } else hearing.hearAgain(ear);
        })).onFail(() -> hearing.hearAgain(ear)); // 다시듣기

        ear.setSpeechRecognizeListener(hearing); // 행동 설정
        ear.startRecording(false); // 듣기 시작
    }


    @Override
    protected void onPause() {
        super.onPause();
        audioManager.setMicrophoneMute(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        audioManager.setMicrophoneMute(false);
    }

    @Override
    public void onBackPressed() {
        onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        onSwipeTouchListener.getGestureDetector().onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }
}