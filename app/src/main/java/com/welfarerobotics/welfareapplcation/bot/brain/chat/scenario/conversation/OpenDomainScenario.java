package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.conversation;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.Hormone;
import com.welfarerobotics.welfareapplcation.bot.brain.Pituitary;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.ModelApi;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.preprocess.NameReplacer;
import com.welfarerobotics.welfareapplcation.bot.face.FaceExpressionGenerator;
import com.welfarerobotics.welfareapplcation.util.Pool;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 1:06 AM
 * @homepage : https://github.com/gusdnd852
 */
public class OpenDomainScenario {
    public static void process(String intent, String speech, Activity activity) throws IOException, ExecutionException, InterruptedException {
        String answer = ModelApi.getOpenDomainAnswer(speech);
        /*원우 추가*/
        ImageView eye = activity.findViewById(R.id.eye); //감정 눈
        ImageView mouth = activity.findViewById(R.id.s_mouth);//감정 입
        /*        */

        if (answer.contains("#")) { // 욕설이 포함된 경우
            answer = answer.replaceAll("#", ""); // 대답에서 # 제거
            speech = speech + "#"; // 사용자 입력에 # 추가
        }
        Brain.hippocampus.decideToSay(NameReplacer.replaceName(answer));
        Mouth.get().say(activity); // 음성 말하기 세션

        // 호르몬 변화 세션
        String finalSpeech = speech;
        Pool.hormoneThread.submit(() -> {
            try {
                Hormone hormone = Pituitary.rememberNewSentence(finalSpeech);
                FaceExpressionGenerator.ganerate(activity, hormone, eye, mouth);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}