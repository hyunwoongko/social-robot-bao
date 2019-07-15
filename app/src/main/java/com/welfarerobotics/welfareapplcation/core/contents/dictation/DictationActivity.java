package com.welfarerobotics.welfareapplcation.core.contents.dictation;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.*;
import com.kinda.alert.KAlertDialog;
import com.myscript.atk.scw.SingleCharWidget;
import com.myscript.atk.scw.SingleCharWidgetApi;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.entity.DictationQuiz;
import com.welfarerobotics.welfareapplcation.core.base.VoiceActivity;
import com.welfarerobotics.welfareapplcation.util.Sound;
import com.welfarerobotics.welfareapplcation.util.ToastType;
import es.dmoral.toasty.Toasty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DictationActivity extends VoiceActivity implements SingleCharWidgetApi.OnTextChangedListener {
    private MyScriptBuilder builder;
    private SingleCharWidgetApi widget;
    private List<DictationQuiz> quizzes = new ArrayList<>();
    private DictationQuiz currentDictationQuiz;
    private Random random = new Random();
    private short currentQuestionCount = 1;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private ImageView hintImage;
    private TextView tv;
    private TextView preview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictation);
        ImageButton clear = findViewById(R.id.clear_btn);
        clear.setAlpha(0.5f);
        ImageButton speaker = findViewById(R.id.speaker_btn);
        ImageButton backbtn = findViewById(R.id.backbutton);
        ImageButton hint = findViewById(R.id.hint_btn);
        hintImage = findViewById(R.id.hint_image);
        tv = findViewById(R.id.dictation_write);
        preview = findViewById(R.id.preivew);
        preview.setAlpha(0.8f);

        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("로딩 중...");
        pDialog.setContentText("\n데이터를 로딩하는 중입니다.\n\n");
        pDialog.setCancelable(false);
        pDialog.show();

        clear.setOnClickListener(view -> {
            Toasty.info(this, "초기화합니다.", Toast.LENGTH_SHORT).show();
            widget.clear();
        });

        speaker.setOnClickListener(view -> speak());

        backbtn.setOnClickListener(view -> onBackPressed());

        hint.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    hintImage.setVisibility(View.VISIBLE);
                    return true;

                case MotionEvent.ACTION_MOVE:
                    return true;

                case MotionEvent.ACTION_UP:
                    hintImage.setVisibility(View.INVISIBLE);
                    return false;
            }
            return false;
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("word");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                for (DataSnapshot obj : snapshots) {
                    DictationQuiz quiz = obj.getValue(DictationQuiz.class);
                    quizzes.add(quiz);
                }

                currentDictationQuiz = quizzes.get(random.nextInt(quizzes.size() - 1));
                pDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText("안내");
                pDialog.setContentText("단어를 잘 듣고 받아 적어봅시다.\n" + currentQuestionCount + "번째 문제입니다.");
                pDialog.setCancelable(false);
                pDialog.setConfirmText("예");
                pDialog.setConfirmClickListener(d -> {
                    quizzes.remove(currentDictationQuiz);
                    currentDictationQuiz = quizzes.get(random.nextInt(quizzes.size() - 1));
                    preview.setText(currentDictationQuiz.getWord());
                    speak();
                    Glide.with(DictationActivity.this)
                            .load(currentDictationQuiz.getImageURL())
                            .apply(new RequestOptions().override(200, 200)
                                    .placeholder(R.drawable.hint_notload))
                            .into(hintImage);
                    pDialog.dismissWithAnimation();
                });
                pDialog.confirmButtonColor(R.color.confirm_button);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });

        widget = (SingleCharWidget) findViewById(R.id.widget);
        ((SingleCharWidget) widget).setAlpha(0.8f);
        widget.setOnTextChangedListener(this);
        widget.setInkColor(Color.BLACK);
        widget.setInkWidth(18f);
        builder = new MyScriptBuilder(widget, this);
        builder.Build();
        widget.setAutoCommitEnabled(false);
    }

    @Override
    public void onTextChanged(SingleCharWidgetApi singleCharWidgetApi, String s, boolean b) {
        s = s.replaceAll(" ", "").trim();
        tv.setText(s);
        if (s.contains(currentDictationQuiz.getWord())) {
            quizzes.remove(currentDictationQuiz);
            currentDictationQuiz = quizzes.get(random.nextInt(quizzes.size() - 1));
            Glide
                    .with(DictationActivity.this)
                    .load(currentDictationQuiz.getImageURL())
                    .apply(new RequestOptions().override(200, 200)
                            .placeholder(R.drawable.hint_notload))
                    .into(hintImage);
            Sound.get().effectSound(this, R.raw.dingdong);
            showToast("정답입니다", ToastType.success);
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, "정답을 맞췄어요! 다음문제를 풀어볼까요?");
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
            pDialog.setContentText("참 잘했어요.\n모든 문제를 맞췄습니다!!");
            pDialog.setCancelable(false);
            pDialog.setConfirmText("확인");
            pDialog.setConfirmClickListener(d -> finish());
            pDialog.confirmButtonColor(R.color.confirm_button);
            pDialog.show();
        } else {
            KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE);
            pDialog.setTitleText("안내");
            pDialog.setContentText(currentQuestionCount + "번째 문제입니다.\n준비되었나요?");
            pDialog.setConfirmText("예");
            pDialog.confirmButtonColor(R.color.confirm_button);
            pDialog.setConfirmClickListener(d -> {
                preview.setText(currentDictationQuiz.getWord());

                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
                speak();
                pDialog.dismissWithAnimation();
            });
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sound.get().resume(this, R.raw.dictation);
        Sound.get().loop(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Sound.get().pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Sound.get().stop();
    }

    private void speak() {
        mediaPlayer.release();
        mediaPlayer = new MediaPlayer();

        StringBuilder speech = new StringBuilder();
        for (int i = 0; i < currentDictationQuiz.getWord().length(); i++) {
            speech.append(String.valueOf(currentDictationQuiz.getWord().charAt(i)));
            speech.append(" ");
        }

        playVoice(mediaPlayer, speech.toString());
    }
}