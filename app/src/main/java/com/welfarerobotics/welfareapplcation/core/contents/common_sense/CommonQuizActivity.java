package com.welfarerobotics.welfareapplcation.core.contents.common_sense;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.*;
import com.kinda.alert.KAlertDialog;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.VoiceActivity;
import com.welfarerobotics.welfareapplcation.entity.Quiz;
import com.welfarerobotics.welfareapplcation.util.Sound;
import com.welfarerobotics.welfareapplcation.util.ToastType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommonQuizActivity extends VoiceActivity {
    private TextView txtview;
    private List<Quiz> quizzes = new ArrayList<>();
    private Quiz currentQuiz;
    private Random random = new Random();
    private short correctAnswerCount = 0;
    private short currentQuestionCount = 0;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean isRightAnswer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_sense_quiz);
        txtview = findViewById(R.id.question_view);
        ImageView o_view = findViewById(R.id.o_icon);
        ImageView x_view = findViewById(R.id.x_icon);
        ImageButton backbtn = findViewById(R.id.backbutton);

        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("로딩 중...");
        pDialog.setContentText("\n데이터를 로딩하는 중입니다.\n\n");
        pDialog.setCancelable(false);
        pDialog.show();

        o_view.setOnClickListener(v -> select(true));
        x_view.setOnClickListener(v -> select(false));
        backbtn.setOnClickListener(view -> onBackPressed());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("common sense quiz");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                for (DataSnapshot obj : snapshots) {
                    Quiz quiz = obj.getValue(Quiz.class);
                    quizzes.add(quiz);
                }

                currentQuiz = quizzes.get(random.nextInt(quizzes.size() - 1));
                txtview.setText(currentQuiz.getQuestion());
                pDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText("문제");
                pDialog.setContentText("\n문제를 잘 듣고 정답을 맞춰봐요.\n\n");
                pDialog.setCancelable(false);
                pDialog.setConfirmText("확인");
                pDialog.setConfirmClickListener(d -> {
                    quizzes.remove(currentQuiz);
                    currentQuiz = quizzes.get(random.nextInt(quizzes.size() - 1));
                    txtview.setText(currentQuiz.getQuestion());
                    playVoice(mediaPlayer, currentQuiz.getQuestion());
                    pDialog.dismissWithAnimation();
                });
                pDialog.confirmButtonColor(R.color.confirm_button);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });

    }

    private void select(boolean userAnswer) {
        isRightAnswer = currentQuiz.isAnswer() == userAnswer;
        if (isRightAnswer) {
            currentQuestionCount++;
            correctAnswerCount++;
            quizzes.remove(currentQuiz);
            currentQuiz = quizzes.get(random.nextInt(quizzes.size() - 1));
            txtview.setText(currentQuiz.getQuestion());
            Sound.get().effectSound(this, R.raw.dingdong);
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, "정답을 맞췄어요!. 다음 문제를 풀어볼까요?");
            showToast("정답입니다", ToastType.success);
        } else {
            Sound.get().effectSound(this, R.raw.beebeep);
            try{
                mediaPlayer.release();
            }catch (Exception e){


            }

            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, "아니에요. 다시 생각해볼까요?");
            showToast("오답입니다", ToastType.error);
        }

        if (currentQuestionCount == 5) {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, "놀이 종료, 모든 문제를 잘 맞췄어요 !");

            KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("놀이 종료");
            pDialog.setContentText("\n모든 문제를 잘 맞췄어요 !\n\n");
            pDialog.setCancelable(false);
            pDialog.setConfirmText("확인");
            pDialog.setConfirmClickListener(kAlertDialog -> finish());
            pDialog.confirmButtonColor(R.color.confirm_button);
            pDialog.show();
        } else {
            if (isRightAnswer) {
                KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("문제");
                pDialog.setContentText("\n문제를 잘 듣고 정답을 맞춰봐요.\n\n");
                pDialog.setCancelable(false);
                pDialog.setConfirmText("확인");
                pDialog.setConfirmClickListener(d -> {
                    mediaPlayer.release();
                    mediaPlayer = new MediaPlayer();
                    playVoice(mediaPlayer, currentQuiz.getQuestion());
                    pDialog.dismissWithAnimation();
                });
                pDialog.confirmButtonColor(R.color.confirm_button);
                pDialog.show();
            }
        }

    }

    @Override protected void onResume() {
        super.onResume();
        Sound.get().resume(this, R.raw.common_sense);
        Sound.get().loop(true);
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