package com.welfarerobotics.welfareapplcation;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private Intent i;
    private SpeechRecognizer mRecognizer;
    private TextView tv;
    private final int PERMISSION = 1;
    private TextToSpeech tts;
    private ImageView img;
    private String speech;
    private Timer timer;
    private TimerTask ttListen, ttBlink;
    private VideoView vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.txt1);
        img = findViewById(R.id.face);
        vv = findViewById(R.id.videoview);

        //PERMISSION CHECK
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO}, PERMISSION);

        //FULLSCREEN
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

/*      //수정중
        //TIMERTASK(Listening)
        ttListen = new TimerTask() {
            @Override
            public void run() {
                mRecognizer.destroy();
                mRecognizer.startListening(i);
            }
        };
*/

        //VIDEO(BLINKING EYES)
        MediaController mediaController = new MediaController(MainActivity.this);
        mediaController.setVisibility(View.GONE);
        vv.setMediaController(mediaController);
        vv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.close));
        vv.start();

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                img.bringToFront();
            }
        });

        //TIMERTASK(VIDEO PLAYBACK)
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

        IntentFilter filter = new IntentFilter();
        filter.addAction(TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        registerReceiver(m_br, filter);

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(i);
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(), "음성인식 시작", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
            if (timer != null) {
                timer.cancel();
            }
        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            if (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
                if (timer == null) {
                    System.out.println("speech timeout");
                    timer = new Timer();
                    //  timer.schedule(ttListen, 5000, 10000);
                    timer.schedule(ttBlink, 7000, 7000);
                }
            }
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            speech = matches.get(0).replace(" ", ""); //0번이 가장 다듬어진 문장
            tv.setText(speech);
            tts = new TextToSpeech(MainActivity.this, MainActivity.this);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    @Override
    public void onInit(int status) {
        if (speech.matches(".*안녕하세요.*")) {
            img.setImageResource(R.drawable.face2t);
            tts.speak("안녕하세요", TextToSpeech.QUEUE_FLUSH, null, null);
        } else if (speech.matches(".*안녕히계세요.*")) {
            img.setImageResource(R.drawable.face2t);
            tts.speak("안녕히 가세요", TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private BroadcastReceiver m_br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String act = intent.getAction();
            if (act.equals(TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED)) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecognizer.startListening(i);
                        img.setImageResource(R.drawable.face1t);
                    }
                }, 1000);
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(m_br);
        if (mRecognizer != null) {
            mRecognizer.destroy();
        }
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
