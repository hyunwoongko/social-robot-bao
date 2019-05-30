package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario;

import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.named_entity.TranslateEntityRecognizer;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.response.TranslateResponseGenerator;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.state.ChatState;

import java.io.IOException;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 2:27 AM
 * @homepage : https://github.com/gusdnd852
 */
public class TranslateScenario {
    public static ChatState process(String speech, Runnable... forgets) throws IOException {
        List<String>[] entities = TranslateEntityRecognizer.recognize(speech);
        for (Runnable forget : forgets) forget.run(); // 원하는 만큼 기억을 잊음.
        Brain.hippocampus.rememberTrainslate(entities); // 해마에 저장
        String res = TranslateResponseGenerator.response();
        if (res.equals("good")) {
            return ChatState.NORMAL_STATE;
        } else if (res.equals("no lang")) {
            Brain.hippocampus.decideToSay("어떤 언어로 번역해 드릴까요?");
            return ChatState.QUESTION_STATE;
        } else if (res.equals("no word")) {
            Brain.hippocampus.decideToSay("어떤 말을 번역해 드릴까요?");
            return ChatState.QUESTION_STATE;
        } else {
            Brain.hippocampus.decideToSay("죄송해요. 다시 한번 말해주세요");
            return ChatState.QUESTION_STATE;
        }
    }
}
