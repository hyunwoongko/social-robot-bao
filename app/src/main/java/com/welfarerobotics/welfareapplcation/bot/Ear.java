package com.welfarerobotics.welfareapplcation.bot;

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
public class Ear implements SpeechRecognizeListener {

    private Consumer<String> onSuccess;
    private Runnable onFail;
    private Handler handler = new Handler(); // STT 핸들러

    public void hearAgain(SpeechRecognizerClient ear) {
        handler.postDelayed(() -> ear.startRecording(false), 100);
    }

    public boolean isNameDetected(String speech) {
        speech = speech.trim();
        speech = speech.toLowerCase();
        return speech.contains("바우") ||
                speech.contains("바오") ||
                speech.contains("바보") ||
                speech.contains("바보야") ||
                speech.contains("야") ||
                speech.contains("봐봐") ||
                speech.contains("여기");
    }

    public Ear onFail(Runnable onFail) {
        this.onFail = onFail;
        return this;
    }

    public Ear onSuccess(Consumer<String> onSuccess) {
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
        System.out.println("카카오 STT 에러 : " + errorCode);
        System.out.println(errorMsg);
        onFail.run();
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
