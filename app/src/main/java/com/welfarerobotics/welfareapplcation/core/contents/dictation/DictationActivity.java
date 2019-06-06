package com.welfarerobotics.welfareapplcation.core.contents.dictation;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.firebase.database.*;
import com.kinda.alert.KAlertDialog;
import com.myscript.atk.scw.SingleCharWidget;
import com.myscript.atk.scw.SingleCharWidgetApi;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import com.welfarerobotics.welfareapplcation.util.Sound;
import com.welfarerobotics.welfareapplcation.util.ToastType;
import es.dmoral.toasty.Toasty;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

public class DictationActivity extends BaseActivity implements SingleCharWidgetApi.OnTextChangedListener {
    private MyScriptBuilder builder;
    private SingleCharWidgetApi widget;
    private String[] quiz;
    private String text = null;
    private String currentDictation;
    private Random random = new Random();
    private short currentQuestionCount = 1;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String imageURL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictation);
        ImageButton submit = findViewById(R.id.submit_btn);
        ImageButton clear = findViewById(R.id.clear_btn);
        ImageButton speaker = findViewById(R.id.speaker_btn);
        ImageButton backbtn = findViewById(R.id.backbutton);
        //ImageView image = findViewById(R.id.image_dictation);

        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("로딩 중...");
        pDialog.setContentText("\n데이터를 로딩하는 중입니다.\n\n");
        pDialog.setCancelable(false);
        pDialog.show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("dictation");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                quiz = dataSnapshot
                        .getValue()
                        .toString()
                        .replace("[", "")
                        .replace("]", "")
                        .replace(" ", "")
                        .split(",");

                currentDictation = quiz[random.nextInt(quiz.length)];
                pDialog.dismissWithAnimation();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });

        KAlertDialog pDialog2 = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE);
        pDialog2.setTitleText("안내");
        pDialog2.setContentText("단어를 잘 듣고 받아 적어봅시다.\n" + currentQuestionCount + "번째 문제입니다.");
        pDialog2.setConfirmText("예");
        pDialog2.confirmButtonColor(R.color.confirm_button);
        pDialog2.setConfirmClickListener(kAlertDialog -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
            }
            playVoice(mediaPlayer, currentDictation);
            kAlertDialog.dismissWithAnimation();
        });
        pDialog2.setCancelable(false);
        pDialog2.show();

        //TODO
        /*
        try {
            imageURL = ImageCrawler.crawler(currentDictation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Glide
                .with(this)
                .load(imageURL)
                .apply(new RequestOptions().override(300,300))
                .into(image);
         */

        widget = (SingleCharWidget) findViewById(R.id.widget);
        widget.setOnTextChangedListener(this);
        builder = new MyScriptBuilder(widget, this);
        builder.Build();
        widget.setInkFadeOutEffect(1);

        /*위젯 설정*/

        submit.setOnClickListener(view -> {
            if (text != null) // null 체크
                Toasty.success(this, "입력한 단어 : " + text, Toast.LENGTH_SHORT).show();
        });

        clear.setOnClickListener(view -> {
            Toasty.info(this, "초기화합니다.", Toast.LENGTH_SHORT).show();
            widget.clear();
           text = null;
        });

        speaker.setOnClickListener(view -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
            }
            playVoice(mediaPlayer, currentDictation);
        });

        backbtn.setOnClickListener(view -> onBackPressed());

    }

    @Override
    public void onTextChanged(SingleCharWidgetApi singleCharWidgetApi, String s, boolean b) {
        text = s;
        System.out.println(s);
        System.out.println(currentDictation);
        if (s.contains(currentDictation)) {
            Sound.get().effectSound(this, R.raw.dingdong);
            showToast("정답입니다", ToastType.success);
            currentQuestionCount++;
            widget.clear();
            select();
        }
    }

    private void select() {
        // 5문제(1,2,3,4,5)를 풀었다면 다시 할지 묻고, 아니면 다음 문제 안내 디이얼로그 표시
        if (currentQuestionCount == 6) {
            KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("놀이 종료");
            pDialog.setContentText("게임을 다시 할까요?\n\n");
            pDialog.setCancelable(false);
            pDialog.setConfirmText("아니오");
            pDialog.setConfirmClickListener(d -> finish());
            pDialog.setCancelText("예");
            pDialog.setCancelClickListener(d -> {
                finish();
                startActivity(getIntent());
            });
            pDialog.confirmButtonColor(R.color.confirm_button);
            pDialog.cancelButtonColor(R.color.confirm_button);
            pDialog.show();
        } else {
            currentDictation = quiz[random.nextInt(quiz.length)];
            KAlertDialog pDialog2 = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE);
            pDialog2.setTitleText("안내");
            pDialog2.setContentText(currentQuestionCount + "번째 문제입니다.\n준비되었나요?");
            pDialog2.setConfirmText("예");
            pDialog2.confirmButtonColor(R.color.confirm_button);
            pDialog2.setConfirmClickListener(kAlertDialog -> {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = new MediaPlayer();
                }
                playVoice(mediaPlayer, currentDictation);
                kAlertDialog.dismissWithAnimation();
            });
            pDialog2.setCancelable(false);
            pDialog2.show();
        }
    }

    private void playVoice(MediaPlayer mediaPlayer, String tts) {
        Thread thread = new Thread(() -> {
            try {
                String text = URLEncoder.encode(tts, "UTF-8"); // 13자
                String apiURL = "https://naveropenapi.apigw.ntruss.com/voice/v1/tts";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", ServerCache.getInstance().getCssid());
                con.setRequestProperty("X-NCP-APIGW-API-KEY", ServerCache.getInstance().getCsssecret());
                // post request
                String postParams = "speaker=jinho&speed=2.5&text=" + text;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    InputStream is = con.getInputStream();
                    int read = 0;
                    byte[] bytes = new byte[1024];
                    //NaverCSS 폴더 생성
                    File dir = new File(Environment.getExternalStorageDirectory() + "/", "NaverCSS");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    // 랜덤한 이름으로 mp3 파일 생성
                    String tempname = "navercssdictation";
                    File f = new File(Environment.getExternalStorageDirectory() +
                            File.separator + "NaverCSS/" + tempname + ".mp3");
                    f.createNewFile();
                    OutputStream outputStream = new FileOutputStream(f);
                    while ((read = is.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }
                    is.close();

                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    System.out.println(response.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String tempname = "navercssdictation";
        String Path_to_file = Environment.getExternalStorageDirectory() +
                File.separator + "NaverCSS/" + tempname + ".mp3";

        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(Path_to_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    @Override protected void onResume() {
        super.onResume();
        Sound.get().resume(this, R.raw.dictation);
    }

    @Override protected void onPause() {
        super.onPause();
        Sound.get().pause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        Sound.get().stop();
    }
}