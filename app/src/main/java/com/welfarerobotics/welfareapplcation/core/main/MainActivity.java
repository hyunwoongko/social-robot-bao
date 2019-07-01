package com.welfarerobotics.welfareapplcation.core.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.Hormone;
import com.welfarerobotics.welfareapplcation.bot.brain.Pituitary;
import com.welfarerobotics.welfareapplcation.bot.face.Detect;
import com.welfarerobotics.welfareapplcation.bot.face.FaceExpressionGenerator;
import com.welfarerobotics.welfareapplcation.bot.face.FaceHandler;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.initial.SplashActivity;
import com.welfarerobotics.welfareapplcation.entity.Conversation;
import com.welfarerobotics.welfareapplcation.entity.cache.UserCache;
import com.welfarerobotics.welfareapplcation.util.Pool;
import com.welfarerobotics.welfareapplcation.util.Sound;
import com.welfarerobotics.welfareapplcation.util.ToastType;
import com.welfarerobotics.welfareapplcation.util.bluetooth.Bluetooth;
import com.welfarerobotics.welfareapplcation.util.data_loader.DataLoader;
import com.welfarerobotics.welfareapplcation.util.touch_util.ConcreteSwipeTouchListener;
import com.welfarerobotics.welfareapplcation.util.touch_util.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private OnSwipeTouchListener onSwipeTouchListener;
    private AudioManager audioManager;
    private ImageView refresh_view;
    private int STT_RQCODE = 852;
    private SpeechRecognizerClient client;
    private ImageView eyes;
    private ImageView mouth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        eyes = (ImageView) findViewById(R.id.eye);
        mouth = (ImageView) findViewById(R.id.s_mouth);
        TextView emotion = (TextView) findViewById(R.id.emotion);
        onSwipeTouchListener = new ConcreteSwipeTouchListener(this, audioManager, this::promptSpeechInput);
        refresh_view = (ImageView) findViewById(R.id.refresh);
        refresh_view.setOnClickListener(view -> this.refresh());
        Bluetooth.getInstance(this);
        Handler handler = new FaceHandler(eyes, emotion, mouth, this);
        new Detect(handler);
        DataLoader.onDataLoad(); // 모든 데이터 다운로드
    }


    @Override public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_on, R.anim.activity_off);
    }

    @Override public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_on, R.anim.activity_off);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Hormone hormone = Pituitary.getHormone();
            FaceExpressionGenerator.ganerate(this, hormone, eyes, mouth);
            client = new SpeechRecognizerClient.Builder()
                    .setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WEB)
                    .setGlobalTimeOut(30)
                    .build();
            client.setSpeechRecognizeListener(new SpeechListener(
                    ss -> promptSpeechInput(),
                    () -> {
                        startActivity(getIntent());
                        finish();
                    }));
            client.startRecording(false);
        } catch (Throwable e) {
            showToast(e.getMessage(), ToastType.error);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        client.stopRecording();
        client.cancelRecording();
        client = null;

        audioManager.setMicrophoneMute(false);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float fingerX = ev.getX();
        float fingerY = ev.getY();
        float rfButtonX = refresh_view.getX();
        float rfButtonY = refresh_view.getY();
        if (Math.abs(fingerX - rfButtonX) < 75 && Math.abs(fingerY - rfButtonY) < 75) {
        } else if (onSwipeTouchListener != null) {
            onSwipeTouchListener.getGestureDetector().onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }


    private void refresh() {
        Intent mStartActivity = new Intent(this, SplashActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10, mPendingIntent);
        System.exit(0);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "바오에게 말해요!");
        try {
            startActivityForResult(intent, STT_RQCODE);
        } catch (ActivityNotFoundException ignore) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        audioManager.setMicrophoneMute(true);

        if (requestCode == STT_RQCODE) {
            if (resultCode == RESULT_OK && data != null) {
                Pool.mouthThread.execute(() -> {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String s = result.get(0);
                    boolean isTeachedSpeech = false;
                    for (Conversation c : UserCache.getInstance().getDict()) {
                        if (c.getInput().replaceAll(" ", "").trim().equals(
                                s.replaceAll(" ", "").trim())) {
                            Brain.hippocampus.decideToSay(c.getOutput()); // 배웠던 말을 그대로 입력
                            isTeachedSpeech = true;
                            break;
                        }
                    }
                    if (isTeachedSpeech) {
                        Mouth.get().say();
                        Mouth.get().stop(() -> audioManager.setMicrophoneMute(false));
                    } else {
                        Sound.get().effectSound(this, R.raw.think);
                        Brain.thinkAndSay(s, this); // 뇌에서 생각해서 말하기
                        Mouth.get().stop(() -> audioManager.setMicrophoneMute(false));
                    }
                });
            }
        }
    }
}