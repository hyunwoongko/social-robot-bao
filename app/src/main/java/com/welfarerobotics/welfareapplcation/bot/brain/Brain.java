package com.welfarerobotics.welfareapplcation.bot.brain;

import android.app.Activity;
import android.widget.TextView;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.Skills;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.intent.IntentClassifier;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.preprocess.Preprocessor;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.conversation.FallbackScenario;
import com.welfarerobotics.welfareapplcation.bot.face.FaceExpression;
import com.welfarerobotics.welfareapplcation.entity.cache.ChatCache;

import java.util.Random;

/**
 * @Author : Hyunwoong
 * @When : 3/23/2019 10:58 PM
 * @Homepage : https://github.com/gusdnd852
 * <p>
 * 뇌를 모방한 클래스
 * 모든 인공지능 처리의 중추
 * State Pattern 으로 구현됨
 */
public final class Brain {
    public static Hippocampus hippocampus = new Hippocampus();
    public static Random random = new Random();

    public static void thinkAndSay(String speech, Activity activity) {
        try {
            TextView textView = activity.findViewById(R.id.emotion);

            String preprocessedSpeech = Preprocessor.preprocess(speech);
            String intent = IntentClassifier.classify(preprocessedSpeech);
            System.out.println("스피치 : " + preprocessedSpeech);
            System.out.println("인텐트 : " + intent);
            Skills.thinkAndSay(intent, preprocessedSpeech, activity);

            if (textView.getText().toString().equals(FaceExpression.neutral.toString()) ||
                    textView.getText().toString().equals("감정")) return;
            else Mouth.get().stop(() -> Mouth.get().play(ChatCache.getInstance()
                        .getEmotion()
                        .get(textView.getText().toString())
                        .get(random.nextInt(ChatCache.getInstance().getEmotion()
                                .get(textView.getText().toString()).size() - 1))));

        } catch (Throwable e) {
            e.printStackTrace(); // <- 개발땐 에러 봐야해
            FallbackScenario.process(); // <- 나중엔 이걸로
        }
    }
}