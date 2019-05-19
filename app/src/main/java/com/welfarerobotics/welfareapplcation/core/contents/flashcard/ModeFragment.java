package com.welfarerobotics.welfareapplcation.core.contents.flashcard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.welfarerobotics.welfareapplcation.R;
import static com.welfarerobotics.welfareapplcation.core.contents.flashcard.FlashcardActivity.index;
public class ModeFragment extends Fragment {
    private LearningFragment learningFragment = null;
    private QuizFragment quizFragment = null;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mode, container, false);

        ImageButton ibLearning = v.findViewById(R.id.modeLearningButton);
        ImageButton ibQuiz = v.findViewById(R.id.modeQuizButton);

        //모드 선택화면을 표시할 때 index를 1로 재설정
        index=1;

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        ibLearning.setOnClickListener(view -> {
            learningFragment = new LearningFragment();
            fragmentTransaction
                    .setCustomAnimations(R.anim.activity_fade_in, R.anim.activity_fade_out)
                    .replace(R.id.container, learningFragment)
                    .commit();
        });

        ibQuiz.setOnClickListener(view -> {
            quizFragment = new QuizFragment();
            fragmentTransaction
                    .setCustomAnimations(R.anim.activity_fade_in, R.anim.activity_fade_out)
                    .replace(R.id.container, quizFragment)
                    .commit();
        });

        return v;
    }
}
