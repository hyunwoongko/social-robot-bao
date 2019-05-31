package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.device;

import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 2:29 AM
 * @homepage : https://github.com/gusdnd852
 */
public class VolumeDownScenario {
    private static List<String> strings = Arrays.asList(
            "알았어요 소리를 줄일게요",
            "볼륨을 줄일게요",
            "네 소리를 줄일게요"
    );

    public static void process(String speech) throws IOException {
        Brain.hippocampus.decideToSay(strings.get(Brain.random.nextInt(strings.size() - 1)));
        // TODO : 볼륨 줄이기 구현해야함
        Mouth.get().say();
    }
}
