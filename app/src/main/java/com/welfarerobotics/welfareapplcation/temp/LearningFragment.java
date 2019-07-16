package com.welfarerobotics.welfareapplcation.temp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

import static com.welfarerobotics.welfareapplcation.core.contents.flashcard.FlashcardActivity.index;

public class LearningFragment extends VoiceFragment {
    private LearningFragment learningFragment = null;
    private FlashcardCache cache = FlashcardCache.getInstance();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String[] name = new String[4];
    private String TAG = "FlashCard";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_learning, container, false);

        //xml 변수 생성
        ImageButton ibleftbtn = v.findViewById(R.id.learningleft);
        ImageButton ibrightbtn = v.findViewById(R.id.learningright);
        ImageButton ib1 = v.findViewById(R.id.learning1);
        ImageButton ib2 = v.findViewById(R.id.learning2);
        ImageButton ib3 = v.findViewById(R.id.learning3);
        ImageButton ib4 = v.findViewById(R.id.learning4);
        TextView tv1 = v.findViewById(R.id.learningText1);
        TextView tv2 = v.findViewById(R.id.learningText2);
        TextView tv3 = v.findViewById(R.id.learningText3);
        TextView tv4 = v.findViewById(R.id.learningText4);
        TextView tvIndex = v.findViewById(R.id.learningIndex);

        //페이지 번호 설정
        tvIndex.setText(String.valueOf(index));

        //이미지버튼과 텍스트뷰 배열 생성
        ImageButton[] ibArray = {ib1, ib2, ib3, ib4};
        TextView[] tvArray = {tv1, tv2, tv3, tv4};

        //페이지 전환에 따른 프래그먼트 생성
        learningFragment = new LearningFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        GradientDrawable border =(GradientDrawable) getContext().getDrawable(R.drawable.word_border);
        /* 각 페이지당 표시할 카드 수는 4개
         * 1페이지 : index=1, 카드 범위=0~3, i의 범위=0~3
         * 2페이지 : index=2, 카드 범위=4~7, i의 범위=4~7
         * 6페이지 : index=6, 카드 범위=20~23, i의 범위=20~23
         */
        for (int i = (index - 1) * 4; i < 4 * index; i++) {
            if (i >= cache.getFlashcard().size()) {
                break;
            }
//            ibArray[i % 4].setBackground(border);
            ibArray[i % 4].setClipToOutline(true);
            FlashCard child = cache.getFlashcard(i);
            Glide
                    .with(this)
                    .load(child.getImageURL())
                    .apply(new RequestOptions().override(226, 320).fitCenter())
                    .into(ibArray[i % 4]);

            name[i % 4] =child.getWord();
            tvArray[i%4].setText(child.getWord());
            Log.d(TAG,child.getImageURL());
            Log.d(TAG,child.getWord());
        }

        //페이지 처음이면 왼쪽 버튼이 invisible
        //페이지 맨끝이면 오른쪽 버튼이 invisible
        if (index == 1) {
            ibleftbtn.setVisibility(View.INVISIBLE);
        }
        if (index == cache.getFlashcard().size() / 4) {
            ibrightbtn.setVisibility(View.INVISIBLE);
        }

        //왼쪽 버튼과 오른쪽 버튼 클릭시 페이지 전환
        ibleftbtn.setOnClickListener(view -> {
            --index;
            fragmentTransaction
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.container, learningFragment, "learningSub")
                    .commit();
        });

        ibrightbtn.setOnClickListener(view -> {
            ++index;
            fragmentTransaction
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.container, learningFragment, "learningSub")
                    .commit();
        });

        //각 이미지 클릭시 읽어주기(TTS) 기능
        ib1.setOnClickListener(view -> {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, name[0]);
        });
        ib2.setOnClickListener(view -> {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, name[1]);
        });
        ib3.setOnClickListener(view -> {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, name[2]);
        });
        ib4.setOnClickListener(view -> {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, name[3]);
        });

        return v;
    }
}
