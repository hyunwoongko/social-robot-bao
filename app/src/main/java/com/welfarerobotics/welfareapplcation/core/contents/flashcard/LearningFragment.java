package com.welfarerobotics.welfareapplcation.core.contents.flashcard;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.VoiceFragment;
import com.welfarerobotics.welfareapplcation.entity.FlashCard;
import com.welfarerobotics.welfareapplcation.entity.cache.FlashcardCache;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static com.welfarerobotics.welfareapplcation.core.contents.flashcard.FlashcardActivity.index;

public class LearningFragment extends VoiceFragment {
    private LearningFragment quizFragment = null;
    private FlashcardCache cache = FlashcardCache.getInstance();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean flag;
    private String name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);
        flag = true;

        //xml 변수 생성
        ImageButton ibQuizImage = v.findViewById(R.id.quizImage);
        ImageButton ibleftbtn = v.findViewById(R.id.quizLeft);
        ImageButton ibrightbtn = v.findViewById(R.id.quizRight);
        TextView tvQuizText = v.findViewById(R.id.quizText);
        TextView tvAnswer = v.findViewById(R.id.quizAnswer);
        tvAnswer.setVisibility(View.GONE);
        tvQuizText.setVisibility(View.VISIBLE);

        //페이지 이동을 위한 변수 생성
        quizFragment = new LearningFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        //각 페이지 별 카드 설정
        FlashCard child = cache.getFlashcard(index - 1);
        Glide
                .with(this)
                .load(child.getImageURL())
                .apply(new RequestOptions().override(225, 319).fitCenter())
                .into(ibQuizImage);
        name = child.getWord();
        tvQuizText.setText(child.getWord());

        //페이지 처음이면 왼쪽 버튼이 invisible
        //페이지 맨끝이면 오른쪽 버튼이 invisible
        if (index == 1) {
           // ibleftbtn.setVisibility(View.INVISIBLE);
        }
        if (index == cache.getFlashcard().size()) {
          //  ibrightbtn.setVisibility(View.INVISIBLE);
        }

        //정답을 확인한 이후에만 읽어주기 기능 동작
        ibQuizImage.setOnClickListener(view -> {
                try{
                    mediaPlayer.release();
                    mediaPlayer = new MediaPlayer();
                    playVoice(mediaPlayer, name);
                }catch (Exception e){

                }


        });

        ibleftbtn.setOnClickListener(view -> {
            --index;
            flag = false;
            tvQuizText.setVisibility(View.INVISIBLE);
            fragmentTransaction
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.container, quizFragment, "quizSub")
                    .commit();
        });

        tvAnswer.setOnClickListener(view -> {
            if (tvQuizText.getVisibility() == View.INVISIBLE) {
                tvQuizText.setVisibility(View.VISIBLE);
                tvAnswer.setText("정답 숨기기");
                flag = true;
            } else {
                tvQuizText.setVisibility(View.INVISIBLE);
                tvAnswer.setText("정답 보기");
                flag = false;
            }
        });

        ibrightbtn.setOnClickListener(view -> {
            ++index;
            flag = false;
            tvQuizText.setVisibility(View.INVISIBLE);
            fragmentTransaction
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.container, quizFragment, "quizSub")
                    .commit();
        });

        return v;
    }

}

