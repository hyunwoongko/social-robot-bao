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
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyPresigningClient;
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
import com.welfarerobotics.welfareapplcation.core.menu.MenuActivity;
import com.welfarerobotics.welfareapplcation.entity.Conversation;
import com.welfarerobotics.welfareapplcation.entity.cache.UserCache;
import com.welfarerobotics.welfareapplcation.util.*;
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
    private ImageView play_with;
    private int STT_RQCODE = 99;
    private SpeechRecognizerClient client;
    private ImageView eyes;
    private ImageView mouth;
    private boolean conversation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        eyes = (ImageView) findViewById(R.id.eye);
        mouth = (ImageView) findViewById(R.id.s_mouth);
        TextView emotion = (TextView) findViewById(R.id.emotion);
        onSwipeTouchListener = new ConcreteSwipeTouchListener(this, audioManager);
        refresh_view = (ImageView) findViewById(R.id.refresh);
        refresh_view.setOnClickListener(view -> this.refresh());
        play_with=findViewById(R.id.play_with);
        play_with.setOnClickListener(view->this.play_with());


        Bluetooth.getInstance(this);
        Handler handler = new FaceHandler(eyes, emotion, mouth, this);
        new Detect(handler);
        DataLoader.onDataLoad(); // 모든 데이터 다운로드




    }

    @Override
    protected void onResume() {
        super.onResume();
        Hormone hormone = Pituitary.getHormone();
        FaceExpressionGenerator.ganerate(this, hormone, eyes, mouth);
        client = new SpeechRecognizerClient.Builder()
                .setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WEB)
                .build();
        client.setSpeechRecognizeListener(new SpeechListener(
                ss -> promptSpeechInput(),
                () -> {
                    overridePendingTransition(R.anim.activity_off, R.anim.activity_off);
                    startActivity(getIntent());
                    overridePendingTransition(R.anim.activity_off, R.anim.activity_off);
                    finish();
                    overridePendingTransition(R.anim.activity_off, R.anim.activity_off);
                }));
        client.startRecording(false);
    }

    @Override protected void onPause() {
        super.onPause();
        if (client != null) {
            client.stopRecording();
            client.cancelRecording();
            client = null;
            audioManager.setMicrophoneMute(false);
        }
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
        if(conversation!=true){
            if (Math.abs(fingerX - rfButtonX) < 75 && Math.abs(fingerY - rfButtonY) < 75) {
            } else if (onSwipeTouchListener != null) {
                onSwipeTouchListener.getGestureDetector().onTouchEvent(ev);
            }

        }

        return super.dispatchTouchEvent(ev);
    }


    private void refresh() {
        //conversation=true;
        promptSpeechInput();
       // conversation=false;
    }

    private void play_with(){

        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent);
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
        if (requestCode == STT_RQCODE) {
            if (resultCode == RESULT_OK && data != null) {
                Pool.mouthThread.execute(() -> {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String s = result.get(0);
                    boolean isTeachedSpeech = false;
                    if (UserCache.getInstance().getDict() != null) {
                        for (Conversation c : UserCache.getInstance().getDict()) {
                            if (c.getInput().replaceAll(" ", "").trim().equals(
                                    s.replaceAll(" ", "").trim())) {
                                Brain.hippocampus.decideToSay(c.getOutput()); // 배웠던 말을 그대로 입력
                                isTeachedSpeech = true;
                                break;
                            }
                        }
                    }
                    if (isTeachedSpeech) {
                        audioManager.setMicrophoneMute(true);
                        // 말을 하고있는 중에만 소리를 막음
                        Mouth.get().say(this);
                        Mouth.get().stop(() -> audioManager.setMicrophoneMute(false));
                        // 말이 끝나면 다시 소리 들음
                    } else {
                        audioManager.setMicrophoneMute(true);
                        // 말을 하고있는 중에만 소리를 막음
                        Sound.get().effectSound(this, R.raw.think);
                        Brain.thinkAndSay(s, this); // 뇌에서 생각해서 말하기
                        Mouth.get().stop(() -> audioManager.setMicrophoneMute(false));
                        // 말이 끝나면 다시 소리 들음
                    }
                });
            }
        }
    }
}