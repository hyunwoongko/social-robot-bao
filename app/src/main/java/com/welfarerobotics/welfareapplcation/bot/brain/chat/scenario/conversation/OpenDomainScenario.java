package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.conversation;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.Pituitary;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.ModelApi;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.preprocess.NameReplacer;

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

        if (answer.contains("#")) { // 욕설이 포함된 경우
            answer = answer.replaceAll("#", ""); // 대답에서 # 제거
            speech = speech + "#"; // 사용자 입력에 # 추가
        }
        Brain.hippocampus.decideToSay(NameReplacer.replaceName(answer));
        Mouth.get().say(); // 먼저 말하고
        Pituitary.setHormone(speech, activity); // 표정 변화 (속도 향상을 위해 어쩔 수 없음)
    }
}
