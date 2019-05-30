package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario;

import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.WiseApi;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.state.ChatState;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 1:15 AM
 * @homepage : https://github.com/gusdnd852
 */
public class WiseScenario {
    public static ChatState process(String speech) throws IOException {
        Brain.hippocampus.decideToSay(WiseApi.getWise());
        return ChatState.NORMAL_STATE;
    }
}
