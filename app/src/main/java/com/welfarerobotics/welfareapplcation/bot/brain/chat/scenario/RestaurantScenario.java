package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.state.ChatState;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/27/2019 7:05 PM
 * @homepage : https://github.com/gusdnd852
 */
public class RestaurantScenario {
    public static ChatState process(String preprocessedSpeech, Runnable... forgets) throws IOException {
        for (Runnable forget : forgets) forget.run();

        return ChatState.NORMAL_STATE;
    }
}
