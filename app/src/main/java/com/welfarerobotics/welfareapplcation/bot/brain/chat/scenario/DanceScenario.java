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
public class DanceScenario {

    private static List<String> strings = Arrays.asList(
            "제가 제일 좋아하는 춤이에요.",
            "이 춤 어때요?",
            "제가 제일 잘 추는 춤이에요",
            "저는 이 춤이 제일 좋더라구요"
    );

    public static ChatState process(String speech) throws IOException {
        Brain.hippocampus.decideToSay(strings.get(Brain.random.nextInt(strings.size() - 1)));
        // TODO : 댄스 기능 구현해야함
        return ChatState.NORMAL_STATE;
    }
}
