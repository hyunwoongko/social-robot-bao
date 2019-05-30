package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario;

import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.state.ChatState;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 2:26 AM
 * @homepage : https://github.com/gusdnd852
 */
public class SightScenario {
    private static List<String> strings = Arrays.asList(
            "지금 쳐다보고 있어요.",
            "보고 있어요",
            "아까부터 보고 있었어요"
    );

    public static ChatState process(String speech) throws IOException {
        Brain.hippocampus.decideToSay(strings.get(Brain.random.nextInt(strings.size() - 1)));
        // TODO : 시선 기능 구현 해야함
        return ChatState.NORMAL_STATE;
    }
}
