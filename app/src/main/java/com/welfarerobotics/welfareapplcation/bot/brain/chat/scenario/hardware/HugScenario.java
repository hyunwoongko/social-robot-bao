package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.hardware;

import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/30/2019 8:39 AM
 * @homepage : https://github.com/gusdnd852
 */
public class HugScenario {
    private static List<String> strings = Arrays.asList(
            "제가 안아줄게요.",
            "이리와요 안아줄게요",
            "이렇게 안아주면 되나요?"
    );

    public static void process(String speech) throws IOException {
        Brain.hippocampus.decideToSay(strings.get(Brain.random.nextInt(strings.size() - 1)));
        // TODO : 포옹 기능 구현 해야함
        Mouth.get().say();
    }
}
