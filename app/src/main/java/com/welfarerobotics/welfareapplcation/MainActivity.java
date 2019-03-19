package com.welfarerobotics.welfareapplcation;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerActivity;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SpeechRecognizeListener {

    private Intent i;
    private TextView tv;
    private ImageView img;
    private String speech, lang;
    private Timer timer;
    private TimerTask ttBlink;
    private VideoView vv;
    private final int PERMISSION = 1;
    private STTRepeatListener mSTTRepeatListener;
    private SpeechRecognizerClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.txt1);
        img = findViewById(R.id.face);
        vv = findViewById(R.id.videoview);
        timer = new Timer();

        //권한 확인
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WAKE_LOCK,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION);

        //전체화면
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //STT 초기화
        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        //STT 클라이언트 생성
        SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
                setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION);
        client = builder.build();

        //비디오(눈 깜빡임)
        MediaController mediaController = new MediaController(MainActivity.this);
        mediaController.setVisibility(View.GONE);
        vv.setMediaController(mediaController);
        vv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.close));
        vv.start(); //최초 재생시 끊김이 있으므로 미리 화면 뒤쪽에서 한번 재생시킴

        //비디오 재생 후 표정이 화면의 맨 앞에 위치
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                img.bringToFront();
            }
        });

        //Timertask(비디오 재생)
        //비디오 재생시 비디오가 화면의 맨 앞에 위치
        ttBlink = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("video playback");
                        vv.bringToFront();
                        vv.start();
                    }
                });
            }
        };

        client.setSpeechRecognizeListener(this);
        client.startRecording(false);


        mSTTRepeatListener = new STTRepeatListener() {
            @Override
            public void onReceivedEvent() {
                System.out.println("이벤트받음++++++++++++++++++++++++++++++++++++++++++++");
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        client.startRecording(false);
                    }
                }, 100);
            }
        };
    }


    //STT가 자동 생성한 콜백 메소드
    @Override
    public void onReady() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "음성인식 시작", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBeginningOfSpeech() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onEndOfSpeech() {
        System.out.println("음성입력 완료");

    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        System.out.println("========에러 번호 : " + errorCode);
        System.out.println(errorMsg);
        mSTTRepeatListener.onReceivedEvent();
    }

    @Override
    public void onPartialResult(String partialResult) {

    }

    @Override
    public synchronized void onResults(Bundle results) {
        ArrayList<String> matches =
                results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        speech = matches.get(0).replace(" ", ""); //0번이 가장 다듬어진 문장
        tv.bringToFront();
        tv.setText(speech);
        new Thread() {
            public void run() {
                if (lang == null) {
                    if (speech.equals("안녕하세요")) {
                        new CSSAPI("반가워요", "mijin");
                        TTSPlayback();
                    } else if (speech.equals("안녕히계세요")) {
                        new CSSAPI("잘가요", "mijin");
                        TTSPlayback();
                    }
                }
            }
        }.start();
    }

    @Override
    public void onAudioLevel(float audioLevel) {

    }

    @Override
    public void onFinished() {

    }

    public synchronized void TTSPlayback() {
        //바로 재생
        String tempname = "navercssfile";
        String Path_to_file = Environment.getExternalStorageDirectory() +
                File.separator + "NaverCSS/" + tempname + ".mp3";
        MediaPlayer audioplay = new MediaPlayer();
        try {
            audioplay.setDataSource(Path_to_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            audioplay.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioplay.start();

        //재생 종료 리스너
        audioplay.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        client.startRecording(false);
                    }
                }, 100);
            }
        });
    }

    //어플리케이션 종료시
    @Override
    protected void onDestroy() {
        SpeechRecognizerManager.getInstance().finalizeLibrary();
        mSTTRepeatListener=null;
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }
}

