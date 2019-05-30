package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario;

import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.named_entity.RestaurentEntityRecognizer;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.state.ChatState;

import java.io.IOException;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/27/2019 7:05 PM
 * @homepage : https://github.com/gusdnd852
 */
public class RestaurantScenario {
    public static ChatState process(String speech, Runnable... forgets) throws IOException {
        List<String> entity = RestaurentEntityRecognizer.recognize(speech);
        if(entity.size() == 0){
            Brain.hippocampus.decideToSay("어떤 맛집을 찾아드릴까요?");
            return ChatState.QUESTION_STATE;
        }
        for(Runnable forget : forgets) forget.run(); // 원하는 만큼 기억을 잊음.
        Brain.hippocampus.rememberLocation(entity);
        return ChatState.NORMAL_STATE;
    }
}
