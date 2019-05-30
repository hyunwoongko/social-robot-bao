package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario;

import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.named_entity.NewsEntityRecognizer;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.named_entity.WikiEntityRecognizer;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.response.NewsResponseGenerator;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.response.WikiResponseGenerator;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.state.ChatState;

import java.io.IOException;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 2:17 AM
 * @homepage : https://github.com/gusdnd852
 */
public class NewsScenario {
    public static ChatState process(String speech, Runnable... forgets) throws IOException {
        List<String> entities = NewsEntityRecognizer.recognize(speech);
        if (entities.size() == 0) {
            Brain.hippocampus.decideToSay("무엇을 알려드릴까요?");
            return ChatState.QUESTION_STATE;
        }
        for (Runnable forget : forgets) forget.run(); // 원하는 만큼 기억을 잊음.
        Brain.hippocampus.rememberWord(entities); // 해마에 엔티티를 기억시킴.
        String response = NewsResponseGenerator.response(entities);
        Brain.hippocampus.decideToSay(response);
        return ChatState.NORMAL_STATE;
    }
}
