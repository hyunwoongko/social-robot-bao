package com.welfarerobotics.welfareapplcation.core.main;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.api.chat.ChatApi;
import com.welfarerobotics.welfareapplcation.api.chat.CssApi;
import com.welfarerobotics.welfareapplcation.api.chat.chatutil.EmotionAdder;
import com.welfarerobotics.welfareapplcation.core.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.contents.tangram.TangramListItem;
import com.welfarerobotics.welfareapplcation.core.contents.tangram.TangramStageCash;
import com.welfarerobotics.welfareapplcation.core.fairytale.FairytaleCache;
import com.welfarerobotics.welfareapplcation.core.menu.MenuActivity;
import com.welfarerobotics.welfareapplcation.entity.Server;
import com.welfarerobotics.welfareapplcation.entity.ServerCache;
import com.welfarerobotics.welfareapplcation.entity.User;
import com.welfarerobotics.welfareapplcation.entity.UserCache;
import com.welfarerobotics.welfareapplcation.util.OnSwipeTouchListener;
import com.welfarerobotics.welfareapplcation.util.RxSchedulers;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements SpeechRecognizeListener {

    private ImageView img;
    private String speech, lang;
    private Timer timer;
    private TimerTask ttBlink;
    private VideoView vv;

    private SpeechRecognizerClient client;
    private OnSwipeTouchListener onSwipeTouchListener;
    private AudioManager audioManager;
    private boolean conversationMode = false;
    private Handler STTHandler, BlinkHandler;
    //블루투스 송수신간에 필요한것들
    private static final String TAG = "BluetoothChatActivity";
    private String emtion_status;
    //current connection status
    static String currentStatus = "not connected";
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    public static final int REQUEST_DIALOG_FRAGMENT = 4;
    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;

    private static boolean isRead = false;

    private static boolean isCountdown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Get local Bluetooth adapter
        mOutStringBuffer = new StringBuffer("");
        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tView = (TextView) view.findViewById(R.id.listItem);
                if (isRead) {
                    tView.setTextColor(Color.BLUE);
                } else {
                    tView.setTextColor(Color.RED);
                }
                return view;
            }
        };
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Get the device MAC address
        String address = "B8:27:EB:C9:9E:54";
        // Get the BluetoothDevice object

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device'
        try {
            mChatService = new BluetoothChatService(this, mHandler);
            mChatService.connect(device, true);
            final Timer bttimer;
            TimerTask timerTask;

            final long time = 30000;
            final long lastTime = System.currentTimeMillis();
            bttimer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    while (true) {
//                       long currentTime = System.currentTimeMillis();
//                       Log.v("", "타이머 작동중 ");
//                       Log.v("", "(currentTime-lastTime)>time : " + ((currentTime - lastTime) > time));
//                        //setupChat()
                        try {
                            Thread.sleep(1000);
                            sendMessage("emotion");
                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public boolean cancel() {
                    Log.v("", "타이머 종료");
                    return super.cancel();
                }
            };
            bttimer.schedule(timerTask, 0, 3000);

        } catch (Exception e) {

            System.out.println(e + "에러 종류가 뭐냐");

        }

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            //            activity.finish();
        }

        img = findViewById(R.id.face);
        vv = findViewById(R.id.videoview);
        ttBlink = BlinkTimerTask();
        timer = new Timer();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        STTHandler = new Handler(Looper.getMainLooper());
        BlinkHandler = new Handler();

        onSwipeTouchListener = new OnSwipeTouchListener(MainActivity.this) {
            @Override
            public void onSwipeTop() {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            }

            @Override
            public void onSwipeBottom() {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
            }

            @Override
            public void onActivityDoubleTap() {
                Intent menuIntent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(menuIntent);
            }
        };

        FirebaseDatabase.getInstance().getReference("server").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Server model = dataSnapshot.getValue(Server.class);
                assert model != null;
                ServerCache.setInstance(model);
                //전체화면
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

                //화면 항상 켜짐
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
                BlinkHandler.post(() ->
                        runOnUiThread(() -> {
                            vv.start(); //최초 재생시 끊김이 있으므로 미리 화면 뒤쪽에서 한번 재생시킴
                        })
                );

                //비디오 재생 후 표정이 화면의 맨 앞에 위치
                vv.setOnCompletionListener(mediaPlayer -> img.bringToFront());

                client.setSpeechRecognizeListener(MainActivity.this);
                client.startRecording(false);

                ttBlink = BlinkTimerTask();
                timer.schedule(ttBlink, 7000);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        fairytaleDataSetting();
        tangramDataSetting();
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {

        Log.d(TAG, "setupChat()");
        String message = "emotion";
        sendMessage(message);
        // Initialize the BluetoothChatService to perform bluetooth connections
        //mChatService = new BluetoothChatService(this, mHandler);
        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            // Toast.makeText(this,"BT not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
//            mOutEditText.setText(mOutStringBuffer); 수신받은내용을 출력해주는부분
            //Toast.makeText(this, mOutStringBuffer, Toast.LENGTH_SHORT).show();
            emtion_status = mOutStringBuffer.toString();
        }
    }

    private void sendMessage(String SSID, String PWD) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, "BT not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (SSID.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            JSONObject mJson = new JSONObject();
            try {
                mJson.put("SSID", SSID);
                mJson.put("PWD", PWD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] send = mJson.toString().getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
//            mOutEditText.setText(mOutStringBuffer); 수신받은내용을 출력해주는부분
            Toast.makeText(this, mOutStringBuffer, Toast.LENGTH_SHORT).show();
        }
    }

    private final Runnable watchDogTimeOut = new Runnable() {
        @Override
        public void run() {
            isCountdown = false;
            //time out
//            if(mProgressDialog.isShowing()){
//                mProgressDialog.dismiss();
//                Toast.makeText(getActivity(),"No response from RPi",Toast.LENGTH_LONG).show();
//            }
        }
    };
    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    isRead = false;
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //Log.d(TAG, "writeMessage = " + writeMessage);
                    mConversationArrayAdapter.add("Command:  " + writeMessage);
                    //Toast.makeText(context,writeMessage,Toast.LENGTH_SHORT);

                    break;
                case Constants.MESSAGE_READ:
                    isRead = true;
//                    byte[] readBuf = (byte[]) msg.obj;
//                     construct a string from the valid bytes in the buffer
//                    String readMessage = new String(readBuf, 0, msg.arg1);
//                    String readMessage = new String(readBuf);
                    String readMessage = (String) msg.obj;
                    Log.d(TAG, "readMessage = " + readMessage);
                    //TODO: if message is json -> callback from RPi
                    if (isJson(readMessage)) {
                        handleCallback(readMessage);
                    } else {
                        if (isCountdown) {
                            mHandler.removeCallbacks(watchDogTimeOut);
                            isCountdown = false;
                        }

                        //remove the space at the very end of the readMessage -> eliminate space between items
                        readMessage = readMessage.substring(0, readMessage.length() - 1);
                        //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                        mConversationArrayAdapter.add(readMessage);
                        Log.d(TAG, "받은 메세지 = " + readMessage);
                        //라즈베리파이에서 받는 메세지 부분 readMessage
                        EmotionAdder.setEmotion(readMessage);
                        Log.d(TAG, "감정 메세지 = " + EmotionAdder.getEmotion());
                        //바뀜
                    }

                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != this) {
//                        Toast.makeText(this, "Connected to "
//                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
//                    if (null != activity) {
//                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
//                                Toast.LENGTH_SHORT).show();
//                    }
                    break;
            }
        }
    };

    public boolean isJson(String str) {
        try {
            new JSONObject(str);
        } catch (JSONException ex) {
            return false;
        }
        return true;
    }

    public void handleCallback(String str) {
        String result;
        String ip;
        if (isCountdown) {
            mHandler.removeCallbacks(watchDogTimeOut);
            isCountdown = false;
        }

//        //enable user interaction
//        mProgressDialog.dismiss();
        try {
            JSONObject mJSON = new JSONObject(str);
            result = mJSON.getString("result") == null ? "" : mJSON.getString("result");
            ip = mJSON.getString("IP") == null ? "" : mJSON.getString("IP");
            //Toast.makeText(getActivity(), "result: "+result+", IP: "+ip, Toast.LENGTH_LONG).show();

            if (!result.equals("SUCCESS")) {
                Toast.makeText(this, "FAIL", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(),getString(R.string.config_success) + ip,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // error handling
            Toast.makeText(this, "SOMETHING WENT WRONG", Toast.LENGTH_LONG).show();
        }

    }

    //STT가 자동 생성한 콜백 메소드
    @Override
    public void onReady() {
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
        STTHandler.postDelayed(() -> client.startRecording(false), 100);
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

        if (ServerCache.getInstance().isState()) {
            Observable.just(null)
                    .subscribeOn(RxSchedulers.networkThread())
                    .map(Null -> User.builder()
                            .id(UserCache.getInstance().getId())
                            .name(UserCache.getInstance().getName())
                            .photo(UserCache.getInstance().getPhoto())
                            .location(UserCache.getInstance().getLocation())
                            .dict(UserCache.getInstance().getDict() == null ? new ArrayList<>() : UserCache.getInstance().getDict())
                            .build())
                    .doOnNext(m -> ChatApi.get().chat(speech, m, this))
                    .doOnNext(m -> CssApi.get().stop(() -> client.startRecording(false)))
                    .observeOn(RxSchedulers.androidThread())
                    .subscribe();
        } else {
            CssApi.get().play("현재 서버 점검 중입니다.", "jinho");
            CssApi.get().stop(() -> client.startRecording(false));
        }
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
        TimerTask tempTask = new TimerTask() {
            @Override
            public void run() {
                BlinkHandler.post(() -> runOnUiThread(() -> {
                            System.out.println("video playback");
                            vv.bringToFront();
                            vv.start();
                        })
                );

            }
        };
        return tempTask;
    }

    @Override
    protected void onStop() {
        super.onStop();
        audioManager.setMicrophoneMute(true);
        ttBlink.cancel();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        audioManager.setMicrophoneMute(false);
        ttBlink.run();
    }

    @Override
    public void onBackPressed() {
        onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        onSwipeTouchListener.getGestureDetector().onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    //어플리케이션 종료시
    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioManager.setMicrophoneMute(false);
        if (client != null) {
            client.cancelRecording();
        }
        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }



    private void tangramDataSetting(){
        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference();
            databaseReference.child("tangram").child("background").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                    Thread thread = new Thread(()->{
                        TangramListItem myItem;
                        System.out.println(dataSnapshot.getValue().toString()+"++++++++++++");
                        Bitmap stage = convertUrl(dataSnapshot.getValue().toString());
                        System.out.println(dataSnapshot.getValue().toString()+"++++++++++++");
                        myItem = new TangramListItem();

                        myItem.setStage(stage);
                        TangramStageCash.getInstance().addImage(myItem);

                    });
                    thread.start();

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                    myAdaterr = new TangramListAdater();
//                    mListView.setAdapter(myAdaterr);
//                    itemSet(urlList);
                      TangramStageCash.getInstance().clear();
                       tangramDataSetting();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

//                    Intent intent = new Intent(CheckView.this, CheckView.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    finish();
//                    startActivity(intent);
                    TangramStageCash.getInstance().clear();
                    tangramDataSetting();

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Throwable a){

        }

    }

    private void fairytaleDataSetting(){
        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference();
            databaseReference.child("fairytale").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    FairytaleCache.getInstance().addFairytale(dataSnapshot.getValue().toString());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    FairytaleCache.getInstance().clear();
                    fairytaleDataSetting();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    FairytaleCache.getInstance().clear();
                    fairytaleDataSetting();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Throwable a){
            a.printStackTrace();
        }

    }

    private Bitmap convertUrl(String urlString){

        try{

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // 서버로 부터 응답 수신
            conn.connect();

            InputStream is = conn.getInputStream(); // InputStream 값 가져오기
            // Bitmap으로 변환
            return BitmapFactory.decodeStream(is);
        }catch (Exception a){
            System.out.println("asdfasdf"+a);
            return null;
        }



    }
}