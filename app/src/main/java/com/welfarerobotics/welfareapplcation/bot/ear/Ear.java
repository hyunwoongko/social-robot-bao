package com.welfarerobotics.welfareapplcation.bot.ear;

import android.os.Handler;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import java8.util.function.Consumer;


/**
 * @author : Hyunwoong
 * @when : 5/29/2019 12:17 AM
 * @homepage : https://github.com/gusdnd852
 * <p>
 * 귀를 모방한 클래스
 */
public class Ear {
    private SpeechRecognizerClient earCanal; // 외이도
    private Cochlea cochlea; // 달팽이관
    private Handler handler = new Handler();

    public Ear() {
        earCanal = new SpeechRecognizerClient.Builder().setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION).build();
        cochlea = new Cochlea();
    }

    public void ifHear(Consumer<String> success) {
        cochlea.ifHear(success);
        earCanal.setSpeechRecognizeListener(cochlea);
    }

    public void ifNotHear(Runnable fail) {
        cochlea.ifNotHear(fail);
        earCanal.setSpeechRecognizeListener(cochlea);
    }

    public void hear() {
        earCanal.startRecording(false);
    }

    public void hearAgain() {
        handler.postDelayed(this::hear, 100);
    }

    public void block() {
        earCanal.stopRecording();
    }
}
