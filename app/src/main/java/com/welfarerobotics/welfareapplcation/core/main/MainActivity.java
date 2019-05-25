package com.welfarerobotics.welfareapplcation.core.main;

import android.media.AudioManager;
import android.os.Bundle;
import android.view.MotionEvent;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.api.chat.ChatApi;
import com.welfarerobotics.welfareapplcation.api.chat.CssApi;
import com.welfarerobotics.welfareapplcation.core.BaseActivity;
import com.welfarerobotics.welfareapplcation.entity.Server;
import com.welfarerobotics.welfareapplcation.entity.ServerCache;
import com.welfarerobotics.welfareapplcation.util.Pool;
import com.welfarerobotics.welfareapplcation.util.STTManager;
import com.welfarerobotics.welfareapplcation.util.data_loader.DataLoader;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;
import com.welfarerobotics.welfareapplcation.util.touch_util.ConcreteSwipeTouchListener;
import com.welfarerobotics.welfareapplcation.util.touch_util.OnSwipeTouchListener;

public class MainActivity extends BaseActivity {

    private OnSwipeTouchListener onSwipeTouchListener;
    private AudioManager audioManager;
    private SpeechRecognizerClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        onSwipeTouchListener = new ConcreteSwipeTouchListener(this, audioManager);
        client = new SpeechRecognizerClient.Builder().setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION).build();

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
        STTManager manager = new STTManager()
                .onSuccess(speech -> Pool.threadPool.execute(() -> {
                    ChatApi.get().chat(speech, this);
                    CssApi.get().stop(() -> client.startRecording(false));
                }))
                .onFail(handler -> handler.postDelayed(() ->
                        client.startRecording(false), 100));

        client.setSpeechRecognizeListener(manager);
        client.startRecording(false);
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