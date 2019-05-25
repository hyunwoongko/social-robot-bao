package com.welfarerobotics.welfareapplcation.core.contents.common_sense.quiz;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.*;
import com.kinda.alert.KAlertDialog;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.api.chat.CssApi;
import com.welfarerobotics.welfareapplcation.core.BaseActivity;
import com.welfarerobotics.welfareapplcation.util.Sound;
import com.welfarerobotics.welfareapplcation.util.ToastType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommonQuizActivity extends BaseActivity {
    private TextView txtview;
    private List<Quiz> quizzes = new ArrayList<>();
    private Quiz currentQuiz;
    private Random random = new Random();
    private short correctAnswerCount = 0;
    private short currentQuestionCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_sense_quiz);
        txtview = findViewById(R.id.question_view);
        ImageView o_view = findViewById(R.id.o_icon);
        ImageView x_view = findViewById(R.id.x_icon);

        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("로딩 중...");
        pDialog.setContentText("\n데이터를 로딩하는 중입니다.\n\n");
        pDialog.setCancelable(false);
        pDialog.show();

        o_view.setOnClickListener(v -> select(true));
        x_view.setOnClickListener(v -> select(false));

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
                pDialog.dismissWithAnimation();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });

    }

    private void select(boolean userAnswer) {
            currentQuestionCount++;
            if (currentQuiz.isAnswer() == userAnswer) {
                correctAnswerCount++;
                Sound.get().effectSound(this, R.raw.dingdong);
                showToast("정답입니다", ToastType.success);
            } else {
                Sound.get().effectSound(this, R.raw.beebeep);
                showToast("오답입니다", ToastType.error);
           }

            if(currentQuestionCount == 5){
                KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("놀이 종료");
                pDialog.setContentText("\n정답 갯수는 " + correctAnswerCount + "개 입니다.\n게임을 다시 할까요?\n\n");
                pDialog.setCancelable(false);
                pDialog.setConfirmText("아니오");
                pDialog.setConfirmClickListener(d->finish());
                pDialog.setCancelText("예");
                pDialog.setCancelClickListener(d->{
                    finish();
                    startActivity(getIntent());
                });
                pDialog.confirmButtonColor(R.color.confirm_button);
                pDialog.cancelButtonColor(R.color.confirm_button);
                pDialog.show();
            }else{
                quizzes.remove(currentQuiz);
                currentQuiz = quizzes.get(random.nextInt(quizzes.size() - 1));
                txtview.setText(currentQuiz.getQuestion());
            }
        }
}
