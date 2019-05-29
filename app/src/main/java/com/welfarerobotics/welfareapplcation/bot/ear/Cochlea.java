package com.welfarerobotics.welfareapplcation.bot.ear;

import android.os.Bundle;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;

import java.util.ArrayList;
import java8.util.function.Consumer;

/**
 * @author : Hyunwoong
 * @when : 5/29/2019 12:29 AM
 * @homepage : https://github.com/gusdnd852
 */
public class Cochlea implements SpeechRecognizeListener {

    private Consumer<String> success;
    private Runnable fail;

    public Cochlea ifHear(Consumer<String> success) {
        this.success = success;
        return this;
    }

    public Cochlea ifNotHear(Runnable fail) {
        this.fail = fail;
        return this;
    }

    @Override public void onReady() {

    }

    @Override public void onBeginningOfSpeech() {

    }

    @Override public void onEndOfSpeech() {

    }

    @Override public void onError(int errorCode, String errorMsg) {
        System.out.println("STT 에러 : " + errorCode + " : " + errorMsg);
        fail.run();
    }

    @Override public void onPartialResult(String partialResult) {

    }

    @Override public void onResults(Bundle results) {
        ArrayList<String> matches =
                results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        String speech = matches.get(0); //0번이 가장 다듬어진 문장
        success.accept(speech);
    }

    @Override public void onAudioLevel(float audioLevel) {

    }

    @Override public void onFinished() {

    }
}
