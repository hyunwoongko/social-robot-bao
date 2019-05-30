package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.state.ChatState;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 2:25 AM
 * @homepage : https://github.com/gusdnd852
 */
public class AlarmScenario {
    public static ChatState process(String speech, Runnable... forgets) throws IOException {
        for(Runnable forget : forgets) forget.run(); // 원하는 만큼 기억을 잊음.

        return ChatState.NORMAL_STATE;
    }
}
