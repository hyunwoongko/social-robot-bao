package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario;

import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.ModelApi;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.state.ChatState;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 1:06 AM
 * @homepage : https://github.com/gusdnd852
 */
public class OpenDomainScenario {
    public static ChatState process(String intent, String speech) throws IOException {
        String answer = ModelApi.getOpenDomainAnswer(speech);
        Brain.hippocampus.decideToSay(answer);
        return ChatState.NORMAL_STATE;
    }
}
