package com.welfarerobotics.welfareapplcation;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;

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
                Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WAKE_LOCK}, PERMISSION);

        //전체화면
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //STT 초기화
        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        //STT 클라이언트 생성
        SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
                setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION);
        final SpeechRecognizerClient client = builder.build();

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
        client.startRecording(true);

        mSTTRepeatListener = new STTRepeatListener() {
            @Override
            public void onReceivedEvent() {
                System.out.println("이벤트받음++++++++++++++++++++++++++++++++++++++++++++");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        client.startRecording(true);
                    }
                },200);
            }
        };
    }

    public void setOnSTTRepeatListener(STTRepeatListener listener) {
        mSTTRepeatListener = listener;
    }

    //STT 콜백 메소드
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
    public void onResults(Bundle results) {
        ArrayList<String> matches =
                results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        speech = matches.get(0).replace(" ", ""); //0번이 가장 다듬어진 문장
        tv.bringToFront();
        tv.setText(speech);
        if (lang == null) {
            if (speech.equals("안녕하세요")) {
                System.out.println("음성처리 시작");
                new Thread() {
                    public void run() {
                        new CSSAPI("반갑습니다", "mijin");
                    }
                }.start();
            }
        }
        mSTTRepeatListener.onReceivedEvent();
    }

    @Override
    public void onAudioLevel(float audioLevel) {

    }

    @Override
    public void onFinished() {

    }

    //어플리케이션 종료
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpeechRecognizerManager.getInstance().finalizeLibrary();
        if (timer != null) {
            timer.cancel();
        }
    }
}

/*

    private RecognitionListener listener = new RecognitionListener() {

       }

        @Override
        public void onError(int error) {

            mRecognizer.startListening(i);
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            speech = matches.get(0).replace(" ", ""); //0번이 가장 다듬어진 문장
            tv.bringToFront();
            tv.setText(speech);
            new CSSAPI(speech);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

 /*   private BroadcastReceiver m_br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String act = intent.getAction();
            if (act.equals(TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED)) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        timer.schedule(ttListen, 5000, 5000);
                        img.setImageResource(R.drawable.face1t);
                    }
                }, 1000);
            }
        }
    };
*/

