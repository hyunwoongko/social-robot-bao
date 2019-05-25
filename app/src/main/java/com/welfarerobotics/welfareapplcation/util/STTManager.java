package com.welfarerobotics.welfareapplcation.util;

import android.os.Bundle;
import android.os.Handler;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import java8.util.function.Consumer;

import java.util.ArrayList;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 1:38 PM
 * @homepage : https://github.com/gusdnd852
 */
public class STTManager implements SpeechRecognizeListener {

    private Handler sttHandler = new Handler();
    private Consumer<String> onSuccess;
    private Consumer<Handler> onFail;

    public STTManager onFail(Consumer<Handler> onFail) {
        this.onFail = onFail;
        return this;
    }

    public STTManager onSuccess(Consumer<String> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    @Override
    public void onReady() {
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        System.out.println("========에러 번호 : " + errorCode);
        System.out.println(errorMsg);
        onFail.accept(sttHandler);
    }

    @Override
    public void onPartialResult(String partialResult) {
    }

    @Override
    public synchronized void onResults(Bundle results) {
        ArrayList<String> matches =
                results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        String speech = matches.get(0); //0번이 가장 다듬어진 문장
        onSuccess.accept(speech);
    }


    @Override
    public void onAudioLevel(float audioLevel) {
    }

    @Override
    public void onFinished() {

    }
}
