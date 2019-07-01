package com.welfarerobotics.welfareapplcation.util;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.dialoid.speech.recognition.SpeechRecognizer;

import com.kakao.sdk.newtoneapi.impl.util.PermissionUtils;

/**
 * 음성인식을 이용하기 위한 기본 클래스. <br/>
 * {@link #initializeLibrary(android.content.Context)}를 호출한 후에
 * {@link SpeechRecognizerClient.Builder}로 {@link SpeechRecognizerClient}를 생성하여
 * 음성인식을 시작하기 위한 작업을 진행할 수 있다.
 *
 * @author Kakao Corp.
 * @version 3.0.0
 * <p>
 * 카카오 개쉣키들 개못만들었네
 * 내가 고쳐 쓴다 ㅡㅡ;;
 * @since 2013
 */
public final class SpeechRecognizerManager {
    private static volatile SpeechRecognizerManager instance;
    private static final String TAG = "KakaoSpeechRecognizer";

    private Context appContext;
    private boolean initialized;

    /**
     * SpeechRecognizerManager 인스턴스를 얻는다.
     *
     * @return SpeechRecognizerManager 인스턴스.
     */
    public static SpeechRecognizerManager getInstance() {
        if (instance == null) {
            synchronized (SpeechRecognizerManager.class) {
                if (instance == null) {
                    SpeechRecognizerManager temp = new SpeechRecognizerManager();
                    instance = temp;
                }
            }
        }
        return instance;
    }

    /**
     * 음성인식 라이브러리 초기화 method. 다른 method 사용전에 반드시 한번은 호출해야 한다.
     *
     * @param context Application Context.
     * @return boolean initialize result.
     */
    public boolean initializeLibrary(Context context) throws IllegalArgumentException {
        if (context == null) {
            throw new IllegalArgumentException("context must not be null!");
        }

        if (initialized) {
            Log.d(TAG, "already initialized the library.");
            return true;
        }

        if (context instanceof Application) {
            appContext = context;
        } else {
            appContext = context.getApplicationContext();
        }

        SpeechRecognizer.initializeLibrary(context);

        if (!PermissionUtils.checkMandatoryPermission(context, Manifest.permission.INTERNET)) {
            return false;
        }
        if (!PermissionUtils.checkMandatoryPermission(context, Manifest.permission.RECORD_AUDIO)) {
            return false;
        }
        if (!PermissionUtils.checkMandatoryPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
            return false;
        }
        if (!PermissionUtils.checkMandatoryPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return false;
        }

        Log.d(TAG, "[speechRecognizer] version : " + getVersion());
        initialized = true;

        return true;
    }

    /**
     * 음성검색 라이브러리 해제 method 라이브러리 사용 후 마지막에 해제해 주어야 한다.
     */
    public void finalizeLibrary() {
        SpeechRecognizer.finalizeLibrary();
        initialized = false;
    }

    /* package */ Context getApplicationContext() {
        return appContext;
    }

    /**
     * 초기화 되었는지를 확인.
     *
     * @return 초기화가 수행되었으면 true.
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * 버전 정보를 확인한다.
     *
     * @return 버전 정보.
     */
    public String getVersion() {
        return "3.0";
    }
}
