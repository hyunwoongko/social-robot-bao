package com.welfarerobotics.welfareapplcation.core.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.welfarerobotics.welfareapplcation.util.ToastType;
import es.dmoral.toasty.Toasty;
import java8.util.function.Consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 2019-07-01 오전 11:50
 * @homepage : https://github.com/gusdnd852
 */
public class SpeechListener implements SpeechRecognizeListener {

    private Consumer<String> success;
    private Runnable fail;
    private Handler handler = new Handler();
    private List<String> attentionSet = Arrays.asList(
            "바오", "바오야", "다우", "다우야", "바우", "바우야", "바보야", "카카오야",
            "타오", "타오야", "하오", "하오야", "bow", "빠우", "빠오"
    );

    private boolean detectCalling(String speech) {
        boolean recognization = false;
        speech = speech.trim();
        speech = speech.toLowerCase();
        for (String attention : attentionSet)
            if (speech.contains(attention)) recognization = true;
        return recognization;
    }

    public SpeechListener(Consumer<String> success, Runnable fail) {
        this.success = success;
        this.fail = fail;
    }

    @Override public void onReady() {

    }

    @Override public void onBeginningOfSpeech() {

    }

    @Override public void onEndOfSpeech() {

    }

    @Override public void onError(int errorCode, String errorMsg) {
        handler.postDelayed(fail, 100);
    }

    @Override public void onPartialResult(String partialResult) {

    }

    @Override public void onResults(Bundle results) {
        ArrayList<String> words =
                results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        String speech = words.get(0); //0번이 가장 다듬어진 문장

        if (speech.contains("바보"))
            speech = speech.replaceAll("바보", "바오");

        if (detectCalling(speech)) {
            success.accept(speech);
        } else {
            handler.postDelayed(fail, 100);
        }
    }

    @Override public void onAudioLevel(float audioLevel) {

    }

    @Override public void onFinished() {

    }
}
