package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.device;

import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 2:25 AM
 * @homepage : https://github.com/gusdnd852
 */
public class AlarmScenario {
    private static List<String> strings = Arrays.asList(
            "알람 화면으로 이동합니다",
            "알람 설정을 위해 이동합니다"
    );

    public static void process(String speech) throws IOException {
        Brain.hippocampus.decideToSay(strings.get(Brain.random.nextInt(strings.size() - 1)));
        // TODO : 알람 액티비티로 이동 !!
        Mouth.get().say();
    }
}
