package com.welfarerobotics.welfareapplcation;

import android.Manifest;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.welfarerobotics.welfareapplcation.chat_api.ApiServer;
import com.welfarerobotics.welfareapplcation.chat_api.ChatApi;
import com.welfarerobotics.welfareapplcation.chat_api.CssApi;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SpeechRecognizeListener {

    private ImageView img;
    private String speech, lang;
    private Timer timer;
    private TimerTask ttBlink;
    private VideoView vv;
    private final int PERMISSION = 1;
    private STTRepeatListener mSTTRepeatListener;
    private SpeechRecognizerClient client;
    private boolean conversationMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.face);
        vv = findViewById(R.id.videoview);
        ttBlink = BlinkTimerTask();
        timer = new Timer();

        //권한 확인
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION);

        FirebaseDatabase.getInstance().getReference("server").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ServerModel model = dataSnapshot.getValue(ServerModel.class);
                ApiServer.SERVER_URL = model.getUrl();
                ApiServer.clientId = model.getCssid();
                ApiServer.clientSecret = model.getCsssecret();
                ApiServer.youtubeKey = model.getYoutubekey();

                //전체화면
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

                //STT 초기화
                SpeechRecognizerManager.getInstance().initializeLibrary(MainActivity.this);

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
                vv.setOnCompletionListener(mediaPlayer -> img.bringToFront());

                client.setSpeechRecognizeListener(MainActivity.this);
                client.startRecording(false);
                mSTTRepeatListener = () -> {
                    System.out.println("이벤트받음++++++++++++++++++++++++++++++++++++++++++++");
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(() -> client.startRecording(false), 300);
                };

                ttBlink = BlinkTimerTask();
                timer.schedule(ttBlink, 7000);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //STT가 자동 생성한 콜백 메소드
    @Override
    public void onReady() {
        runOnUiThread(() ->
                Toast.makeText(getApplicationContext(), "음성인식 시작", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onBeginningOfSpeech() {

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
        conversationMode = false;
    }

    @Override
    public void onPartialResult(String partialResult) {

    }

    //CSS speaker name
    //mijin : 한국어, 여성 / jinho : 한국어, 남성 / clara : 영어, 여성 / matt : 영어, 남성
    //shinji : 일본어, 남성 / meimei : 중국어, 여성 / liangliang : 중국어, 남성 / jose : 스페인어, 남성
    //carmen : 스페인어, 여성
    @Override
    public synchronized void onResults(Bundle results) {
        ArrayList<String> matches =
                results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        speech = matches.get(0); //0번이 가장 다듬어진 문장
        ThreadPool.executor.execute(() -> {
            ChatApi.get(this).chat(speech);
            CssApi.get().stop(() -> client.startRecording(false));
        });
    }


    @Override
    public void onAudioLevel(float audioLevel) {

    }

    @Override
    public void onFinished() {

    }

    //Timertask(비디오 재생)
    //비디오 재생시 비디오가 화면의 맨 앞에 위치
    private TimerTask BlinkTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                ThreadPool.executor.execute(() -> runOnUiThread(() -> {
                    System.out.println("video playback");
                    vv.bringToFront();
                    vv.start();
                }));
            }
        };
    }

    @Override
    public void onBackPressed() {
        client.cancelRecording();
        finish();
        System.exit(0);
    }

    //어플리케이션 종료시
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpeechRecognizerManager.getInstance().finalizeLibrary();
        if (timer != null) {
            timer.cancel();
        }
    }
}