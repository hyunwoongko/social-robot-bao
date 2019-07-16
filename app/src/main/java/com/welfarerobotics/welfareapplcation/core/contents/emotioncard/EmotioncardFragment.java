package com.welfarerobotics.welfareapplcation.core.contents.emotioncard;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.welfarerobotics.welfareapplcation.entity.cache.EmotioncardCache;

import static com.welfarerobotics.welfareapplcation.core.contents.emotioncard.EmotioncardActivity.emotionIndex;

public class EmotioncardFragment extends VoiceFragment {
    private EmotioncardFragment emotioncardFragment = null;
    private EmotioncardCache cache = EmotioncardCache.getInstance();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String[] name = new String[2];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_emotioncard, container, false);

        ImageButton ibleftbtn = v.findViewById(R.id.emotionleft);
        ImageButton ibrightbtn = v.findViewById(R.id.emotionright);
        ImageButton ibEmotion1 = v.findViewById(R.id.emotion1);
        TextView tvIndex = v.findViewById(R.id.emotionIndex);

        tvIndex.setText(String.valueOf(emotionIndex));


        //페이지 전환에 따른 프래그먼트 생성
        emotioncardFragment = new EmotioncardFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        for (int i = (emotionIndex - 1) ; i <  emotionIndex; i++) {
            if (i >= cache.getEmotioncardSize()) {
                break;
            }
            String[] child = cache.getEmotioncard(i);
            Glide
                    .with(this)
                    .load(child[0])
                    .apply(new RequestOptions().override(300, 425).fitCenter())
                    .into(ibEmotion1);

            name[i%1] = child[1];
        }

        //페이지 처음이면 왼쪽 버튼이 invisible
        //페이지 맨끝이면 오른쪽 버튼이 invisible
        if (emotionIndex == 1) {
            ibleftbtn.setVisibility(View.INVISIBLE);
        }
        if (emotionIndex == cache.getEmotioncardSize() / 2) {
            ibrightbtn.setVisibility(View.INVISIBLE);
        }

        //왼쪽 버튼과 오른쪽 버튼 클릭시 페이지 전환
        ibleftbtn.setOnClickListener(view -> {
            --emotionIndex;
            fragmentTransaction
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.container, emotioncardFragment)
                    .commit();
        });

        ibrightbtn.setOnClickListener(view -> {
            ++emotionIndex;
            fragmentTransaction
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.container, emotioncardFragment)
                    .commit();
        });

        //각 이미지 클릭시 읽어주기(TTS) 기능
        ibEmotion1.setOnClickListener(view -> {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, name[0]);
        });

        return v;
    }
}
