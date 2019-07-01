package com.welfarerobotics.welfareapplcation.util;

import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import com.dialoid.speech.recognition.SpeechRecognizer;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.util.helper.CommonProtocol;
import com.kakao.util.helper.SystemInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static com.kakao.util.helper.Utility.getMetadata;

/**
 * 음성인식을 실제로 수행하기 위한 클래스. <br/>
 * {@link #setSpeechRecognizeListener(SpeechRecognizeListener)}으로 callback을 지정한 후에
 * {@link #startRecording(boolean)}으로 녹음을 시작하면 된다. <br/>
 * <br/>
 * 음성합성과 동시에 이용할 수 없다.
 *
 * @author Kakao Corp.
 * @version 3.0.0
 * <p>
 * 카카오 개쉣키들 개못만들었네
 * 내가 고쳐 쓴다 ㅡㅡ;;
 * @since 2013
 */
public final class SpeechRecognizerClient {
    private static final String TAG = "SpeechRecognizerClient";

    /**
     * 음성인식 시작후에 음성입력 시작이 유효한 default timeout. 5초.
     */
    public static final int START_SPEECH_POINT_TIMEOUT = 5;

    /**
     * 전체 인식 시간에 대한 default timeout. 30초. {@link SpeechRecognizerClient.Builder#setGlobalTimeOut(int)}
     */
    public static final int GLOBAL_TIMEOUT_DEFAULT = 30;

    /**
     * 받아쓰기에 특화된 서비스 타입. {@link SpeechRecognizerClient.Builder#setServiceType(String)}
     */
    public static final String SERVICE_TYPE_DICTATION = "DICTATION";

    /**
     * 통합 검색에 특화된 서비스 타입. {@link SpeechRecognizerClient.Builder#setServiceType(String)}
     */
    public static final String SERVICE_TYPE_WEB = "WEB";

    /**
     * 위치 검색에 특화된 서비스 타입. {@link SpeechRecognizerClient.Builder#setServiceType(String)}
     */
    public static final String SERVICE_TYPE_LOCAL = "LOCAL";

    /**
     * 고립어 인식을 위한 서비스 타입. {@link SpeechRecognizerClient.Builder#setServiceType(String)} <br/>
     * {@link SpeechRecognizerClient.Builder#setUserDictionary(String)} 를 통해 설정된 단어 목록에서만 인식한다.
     */
    public static final String SERVICE_TYPE_WORD = "WORD";

    /**
     * 음성입력이 불가능하거나 마이크 접근이 허용되지 않았을 경우.
     * <p>
     * 상수값 : 1
     */
    public static final int ERROR_AUDIO_FAIL = SpeechRecognizer.ErrorCode.ERROR_AUDIO;

    /**
     * apikey 인증이 실패한 경우.
     * <p>
     * 상수값 : 8
     */
    public static final int ERROR_AUTH_FAIL = SpeechRecognizer.ErrorCode.ERROR_SERVER_AUTHENTICATION;

    /**
     * apikey 인증 과정 중 내부 문제가 발생한 경우.
     *
     * @deprecated
     */
    @Deprecated
    public static final int ERROR_AUTH_TROUBLE = 0; //SpeechRecognizer.ErrorCode.ERROR_SERVER_ALLOWED_REQUESTS_EXCESS;

    /**
     * 클라이언트 내부 로직에서 오류가 발생한 경우.
     * <p>
     * 상수값 : 5
     */
    public static final int ERROR_CLIENT = SpeechRecognizer.ErrorCode.ERROR_CLIENT_INETRNAL;

    /**
     * 네트워크 오류가 발생한 경우.
     * <p>
     * 상수값 : 2
     */
    public static final int ERROR_NETWORK_FAIL = SpeechRecognizer.ErrorCode.ERROR_NETWORK;

    /**
     * 네트워크 타임아웃이 발생한 경우.
     * <p>
     * 상수값 : 3
     */
    public static final int ERROR_NETWORK_TIMEOUT = SpeechRecognizer.ErrorCode.ERROR_NETWORK_TIMEOUT;

    /**
     * 서버에서 오류가 발생한 경우.
     * <p>
     * 상수값 : 6
     */
    public static final int ERROR_SERVER_FAIL = SpeechRecognizer.ErrorCode.ERROR_SERVER_INTERNAL;

    /**
     * 서버에서 타임아웃이 발생한 경우.
     * <p>
     * 상수값 : 7
     */
    public static final int ERROR_SERVER_TIMEOUT = SpeechRecognizer.ErrorCode.ERROR_SERVER_TIMEOUT;

    /**
     * 인식된 결과 목록이 없는 경우.
     * <p>
     * 상수값 : 4
     */
    public static final int ERROR_NO_RESULT = SpeechRecognizer.ErrorCode.ERROR_NO_RESULT;

    /**
     * 제공하지 않는 서비스 타입이 지정됐을 경우.
     * <p>
     * 상수값 : 11
     */
    public static final int ERROR_SERVER_UNSUPPORT_SERVICE = SpeechRecognizer.ErrorCode.ERROR_SERVER_UNSUPPORT_SERVICE;

    /**
     * 입력된 사용자 사전에 내용이 없는 경우.
     * <p>
     * 상수값 : 12
     */
    public static final int ERROR_SERVER_USERDICT_EMPTY = SpeechRecognizer.ErrorCode.ERROR_SERVER_USERDICT_EMPTY;

    /**
     * 요청 허용 횟수 초과.
     * <p>
     * 상수값 : 13
     */
    public static final int ERROR_SERVER_ALLOWED_REQUESTS_EXCESS = SpeechRecognizer.ErrorCode.ERROR_SERVER_ALLOWED_REQUESTS_EXCESS;

    /**
     * 전체 소요시간에 대한 타임아웃이 발생한 경우.
     * <p>
     * 상수값 : 99
     */
    public static final int ERROR_RECOGNITION_TIMEOUT = 99;

    /**
     * 결과 문자열 목록을 알 수 있는 키 값. type은 ArrayList&lt;String&gt;. <br/>
     * 첫번째 data 가 입력된 음성과 가장 근접한 인식 결과이며 그 외의 데이터는 후보 결과이다.
     * {@link #KEY_CONFIDENCE_VALUES} 와 같은 개수, 동일한 순서로 제공된다.
     */
    public static final String KEY_RECOGNITION_RESULTS = "recognition_results";

    /**
     * 결과의 confidence 목록을 알 수 있는 키 값. type은 ArrayList&lt;Integer&gt;. <br/>
     * 신뢰도값은 절대값은 아니며 상대적인 값으로서, 0이상의 값을 가진다.
     * {@link #KEY_RECOGNITION_RESULTS} 와 같은 개수, 동일한 순서로 제공된다.
     */
    public static final String KEY_CONFIDENCE_VALUES = "confidence_values";

    /**
     * {@link #KEY_RECOGNITION_RESULTS)}의 첫번째 결과값을 다른 후보 결과를 검토하지 않고, 바로 넘어가도 될 정도의 신뢰도를 가지고 있는지 판단한다.
     */
    public static final String KEY_IS_MARKED_RESULT = "is_marked_result";

    /**
     * 신뢰할 수 있는 값으로 판단할 수 있는 confidence 값 기준 <br/>
     * {@link SpeechRecognizeListener#onResults(android.os.Bundle)} 의 bundle에서 {@link #KEY_CONFIDENCE_VALUES}의 첫번째 값이 이 값 이상이 되면 다른 값을 제안하지 않고{@link #KEY_CONFIDENCE_VALUES}의 첫번째 값을 바로 취해도 되는 기준이 된다. <br/>
     * {@link #KEY_IS_MARKED_RESULT}로 지정되는 값에서 사용한다. <br/>
     */
    public static final int CONFIDENCE_PASSING_MARKED = 5; //10

    private SpeechRecognizeListener speechRecognizeListener;
    private boolean mutedStart;
    private boolean alreadyError;
    private boolean inCancelling;
    private boolean isRecording;
    private boolean beforeReady;

    private SpeechRecognizer speechRecognizer;

    private String apiKey;
    private String appKey;
    private int globalTimeOut;
    private String serviceType;
    private String userDictionary;

    private Timer globalTimer;

    /* package */ SpeechRecognizerClient(SpeechRecognizerClient.Builder builder) {
        apiKey = builder.apiKey;
        appKey = builder.appKey;
        globalTimeOut = builder.globalTimeOut;
        serviceType = builder.serviceType;
        userDictionary = builder.userDictionary;
    }


    private SpeechRecognizer.Listener listener = new SpeechRecognizer.Listener() {
        @Override
        public void onReady() {
            synchronized (SpeechRecognizerClient.this) {
                beforeReady = false;
            }

            if (speechRecognizeListener != null) {
                speechRecognizeListener.onReady();
            }
        }

        @Override
        public void onPartialResult(String text) {
            if (speechRecognizeListener != null) {
                speechRecognizeListener.onPartialResult(text);
            }
        }

        @Override
        public void onInactive() {
            if (inCancelling) {
                // cancelSpeechRecognizer() 에서 필요한 해제작업을 모두 진행하므로 여기서는 pass.
                return;
            }

            new Thread("running-inactive") {
                @Override
                public void run() {
                    synchronized (SpeechRecognizerClient.this) {
                        beforeReady = false;
                        isRecording = false;

                        stopGlobalTimeoutTimer();
                        speechRecognizer = null;
                    }

                    if (speechRecognizeListener != null) {
                        speechRecognizeListener.onFinished();
                    }
                }
            }.start();
        }

        @Override
        public void onFinalResult(String[] texts) {
            onFinalResultConf(texts, null);
        }

        @Override
        public void onFinalResultConf(String[] texts, int[] ints) {
            try {
                if (speechRecognizeListener != null) {
                    Bundle result = new Bundle();

                    ArrayList<String> resultList = new ArrayList<String>(Arrays.asList(texts));
                    ArrayList<Integer> confList = new ArrayList<Integer>();
                    if (ints != null && ints.length > 0) {
                        for (int conf : ints) {
                            confList.add(conf);
                        }
                        result.putBoolean(KEY_IS_MARKED_RESULT, ints[0] >= CONFIDENCE_PASSING_MARKED);
                    }

                    result.putStringArrayList(KEY_RECOGNITION_RESULTS, resultList);
                    result.putIntegerArrayList(KEY_CONFIDENCE_VALUES, confList);

                    speechRecognizeListener.onResults(result);
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);

                onError(ERROR_CLIENT, "VOICE_RECO_RESULT_CONTAIN_INVAID_DATA");
            }
        }

        @Override
        public void onError(int code, String msg) {
            Log.d(TAG, "[onError] : " + code + " msg : " + msg);

            if (alreadyError) {
                return;
            }

            alreadyError = true;
            if (speechRecognizeListener != null) {
                speechRecognizeListener.onError(code, msg);
            }

            cancelRecording(false);
        }

        @Override
        public void onEnergyChanged(int energy) {
            // energy 값은 90 ~ 250의 값이 유효하며,
            // 90 미만의 값은 잡음으로 인식하여 표현하지 않는다.
            if (energy >= 0 && energy < 90) {
                energy = 0;
            }
            float level = (float) energy - 90;
            level /= (250 - 90);
            if (level < 0.0f) {
                level = 0.0f;
            } else if (level > 1.0f) {
                level = 1.0f;
            }

            if (speechRecognizeListener != null) {
                speechRecognizeListener.onAudioLevel(level);
            }
        }

        @Override
        public void onEndPointDetect() {
            if (speechRecognizeListener != null) {
                speechRecognizeListener.onEndOfSpeech();
            }
        }

        @Override
        public void onBeginPointDetect() {
            if (speechRecognizeListener != null) {
                speechRecognizeListener.onBeginningOfSpeech();
            }
        }
    };

    /**
     * callback 설정. 모든 callback method는 ui thread에서 호출되며 비동기 호출이다.
     *
     * @param speechRecognizeListener SpeechRecognizeListener
     */
    public void setSpeechRecognizeListener(final SpeechRecognizeListener speechRecognizeListener) {
        this.speechRecognizeListener = speechRecognizeListener;
    }

    private void startGlobalTimeoutTimer() {
        // TIMER
        globalTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int errorCode = ERROR_RECOGNITION_TIMEOUT;
                String errorString = "GLOBAL_TIMEOUT";

                Context context = SpeechRecognizerManager.getInstance().getApplicationContext();
                ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = conn.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected()) {
                    errorCode = ERROR_NETWORK_FAIL;
                    errorString = "NETWORK_FAILURE";
                }

                if (speechRecognizeListener != null) {
                    speechRecognizeListener.onError(errorCode, errorString);
                }

                cancelRecording(true);
            }
        };

        globalTimer.schedule(timerTask, globalTimeOut * 1000);
    }

    private void stopGlobalTimeoutTimer() {
        if (globalTimer != null) {
            globalTimer.cancel();
            globalTimer = null;
        }
    }

    /**
     * 음성인식 동작을 시작한다.
     *
     * @param mute 음악을 재생중인 경우에 음소거를 할지 여부.
     * @return boolean 성공적으로 시작 되었는지 여부.
     */
    public boolean startRecording(final boolean mute) {
        synchronized (this) {
            if (inCancelling) {
                Handler handler = new Handler();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        startRecording(mute);
                    }
                });

                return true;
            }

            if (isRecording) {
                return false;
            }
        }

        mutedStart = mute;

        final Context context = SpeechRecognizerManager.getInstance().getApplicationContext();

        if (mutedStart) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            } else {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            }
//            AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
//            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }

        synchronized (this) {
            alreadyError = false;

            stopGlobalTimeoutTimer();

            speechRecognizer = SpeechRecognizer.getInstance();

            Method m = null;
            try {
                m = SpeechRecognizer.class.getMethod("setAppID", new Class[]{String.class});
            } catch (NoSuchMethodException e) {
                // no method
            }
            if (m != null) {
                String packageName = context.getPackageName();

                //speechRecognizer.setAppID(packageName);
                try {
                    m.invoke(speechRecognizer, packageName);
                } catch (IllegalAccessException e) {
                    // no method
                } catch (InvocationTargetException e) {
                    // no method
                }
            }

            Context appContext = SpeechRecognizerManager.getInstance().getApplicationContext();
            SystemInfo.initialize(appContext);
            String KAheader = SystemInfo.getKAHeader() + " newtoneSdk/6.0.0";
            Log.d("jack", "KA header : " + KAheader);

            appKey = getMetadata(appContext, CommonProtocol.APP_KEY_PROPERTY);

            speechRecognizer.setAPPKey(appKey);

//            speechRecognizer.setAPIKey(apiKey);
            speechRecognizer.setKAheader(KAheader);
            speechRecognizer.setServer("openapi.voice.search.daum.net", 30000);
//            speechRecognizer.setServer("cheez.voice.search.daum.net", 30000);
//            speechRecognizer.setServer("10.61.28.237", 30000);

            if (!TextUtils.isEmpty(userDictionary)) {
                speechRecognizer.setUserDict(userDictionary);
            }

            speechRecognizer.setListener(listener);
            speechRecognizer.startListening();

            startGlobalTimeoutTimer();

            isRecording = true;
            beforeReady = true;
        }

        return true;
    }


    private void setMuteOff() {
        if (mutedStart) {
            Context appContext = SpeechRecognizerManager.getInstance().getApplicationContext();

            AudioManager audioManager = (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
            } else {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            }

//            AudioManager audioManager = (AudioManager)appContext.getSystemService(Context.AUDIO_SERVICE);
//            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);

            mutedStart = false;
        }
    }

    private void cancelSpeechRecognizer() {
        synchronized (this) {
            if (speechRecognizer != null) {
                // cancel 또는 join 에서 onInactive가 호출된다.
                speechRecognizer.cancel();
            }

            speechRecognizer = null;

            inCancelling = false;
            isRecording = false;
            beforeReady = false;
        }

        setMuteOff();
    }

    private void cancelRecording(boolean sync) {
        synchronized (this) {
            if (inCancelling || beforeReady) {
                return;
            }
            stopGlobalTimeoutTimer();
            inCancelling = true;
        }
        cancelSpeechRecognizer();
    }

    /**
     * 음성인식 동작을 취소한다.
     */
    public void cancelRecording() {
        cancelRecording(false);
    }

    /**
     * 음성인식 녹음을 중지하고, 현재까지 입력된 음성을 통해 인식을 시작한다.
     */
    public void stopRecording() {
        synchronized (this) {
            if (inCancelling || beforeReady) {
                return;
            }

            stopGlobalTimeoutTimer();

            if (speechRecognizer != null) {
                speechRecognizer.stopListening();
            }
        }

        setMuteOff();
    }

    /**
     * {@link SpeechRecognizerClient} 인스턴스를 생성하기 위한 Builder.
     */
    public static class Builder {
        private String apiKey;
        private String appKey;

        private int globalTimeOut = GLOBAL_TIMEOUT_DEFAULT;
        private String serviceType = SERVICE_TYPE_WEB;
        private String userDictionary;

        /**
         * 음성인식을 이용하기 위한 api key. 음성합성과 동일한 key를 이용한다.
         *
         * @return api key 문자열
         */
        @Deprecated
        public String getApiKey() {
            return apiKey;
        }

        /**
         * 음성인식을 이용하기 위한 appkey로 기존 daum에서 등록해서 사용했던 apiKey에서
         * Kakao에서 등록해서 사용하는 값으로 변경. 음성합성과 동일한 key를 이용한다.
         *
         * @return appkey 문자열
         */
        public String getAppKey() {
            return appKey;
        }

        /**
         * api를 이용하기 위해 발급받은 app의 고유 identifier key. <br/>
         * appKey로 변경되었음
         */
        @Deprecated
        public Builder setApiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * api를 이용하기 위해 발급받은 app의 고유 identifier key. <br/>
         * 기존 daum에서 등록해서 사용했던 apiKey에서
         * Kakao에서 등록해서 사용하는 값으로 변경되었으며,
         * 반드시 설정해야 하는 값이다.
         */
        public Builder setAppKey(String appKey) {
            this.appKey = appKey;
            return this;
        }

        /**
         * 녹음과 인식에 전체 소요되는 제한 시간 값. 초단위.
         *
         * @return 초단위의 timeout 값.
         */
        @SuppressWarnings("unused")
        public int getGlobalTimeOut() {
            return globalTimeOut;
        }

        /**
         * 녹음 + 인식 timeout (sec). <br/>
         * 지정 시간동안 인식 결과가 서버로부터 전달이 되지 않으면 error callback 을 호출한다. <br/>
         * 문자열 값이며, 내용은 숫자여야 한다. <br/>
         * {@link #SERVICE_TYPE_DICTATION}을 이용하면 일반적으로 입력이 길어질 수 있기 때문에 주의할 필요가 있다. <br/>
         * 기본값은 {@link SpeechRecognizerClient#GLOBAL_TIMEOUT_DEFAULT}
         *
         * @see {@link SpeechRecognizeListener#onError(int, String)} <br/>
         * {@link #ERROR_RECOGNITION_TIMEOUT}<br/>
         */
        public Builder setGlobalTimeOut(int globalTimeOut) {
            this.globalTimeOut = globalTimeOut;
            return this;
        }

        /**
         * 지정한 serviceType 값을 얻는다.
         *
         * @return serviceType 문자열
         */
        @SuppressWarnings("unused")
        public String getServiceType() {
            return serviceType;
        }

        /**
         * 음성인식 서비스 타입.<br/>
         * {@link #SERVICE_TYPE_WEB}/{@link #SERVICE_TYPE_DICTATION}/{@link #SERVICE_TYPE_LOCAL} 로 구분하여 각각
         * 단어/문장/지역 인식을 선택한다.
         */
        public Builder setServiceType(String serviceType) {
            if (!TextUtils.isEmpty(serviceType)) {
                this.serviceType = serviceType;
            }

            return this;
        }

        /**
         * 지정된 사용자 사전
         *
         * @return 사용자 사전 문자열 값.
         */
        @SuppressWarnings("unused")
        public String getUserDictionary() {
            return userDictionary;
        }

        /**
         * 단어 인식을 위한 사용자 사전을 지정한다. <br/>
         * {@link #SERVICE_TYPE_WORD} 서비스 타입에서 사용되며, 인식결과가 사전에서만 결정된다. 구분자는 '\n'(newline).
         * 예) "현아\n수지\n태연"
         */
        @SuppressWarnings("unused")
        public Builder setUserDictionary(String userDictionary) {
            this.userDictionary = userDictionary;
            return this;
        }

        /**
         * 설정된 정보들을 통해 최종적으로 {@link SpeechRecognizerClient} 인스턴스를 생성한다.
         *
         * @return SpeechRecognizerClient 인스턴스
         * @throws IllegalArgumentException
         */
        public SpeechRecognizerClient build() throws IllegalArgumentException {
//            if(TextUtils.isEmpty(apiKey)) {
//                throw new IllegalArgumentException("API Key is empty. You must set API key!");
//            }

//

            return new SpeechRecognizerClient(this);
        }
    }
}
