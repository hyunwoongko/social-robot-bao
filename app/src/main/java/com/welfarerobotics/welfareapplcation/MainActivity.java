package com.welfarerobotics.welfareapplcation;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private Intent i;
    private SpeechRecognizer mRecognizer;
    private TextView tv;
    private final int PERMISSION = 1;
    private TextToSpeech tts;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //PERMISSION CHECK
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO}, PERMISSION);

        //FULLSCREEN
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv = findViewById(R.id.txt1);
        img = findViewById(R.id.face);

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
                mRecognizer.startListening(i);
            }
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (String match : matches) {
                tv.setText(match);
            }
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
        if (tv.getText().toString().matches(".*안녕하세요.*")) {
            img.setImageResource(R.drawable.face2t);
            tts.speak("안녕하세요", TextToSpeech.QUEUE_FLUSH, null, null);
        } else if (tv.getText().toString().matches(".*안녕히계세요.*")) {
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
                }, 2000);
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(m_br);
        tts.stop();
        tts.shutdown();
        super.onDestroy();
    }
}
