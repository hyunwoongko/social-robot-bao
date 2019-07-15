package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.device;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.util.Volume;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 2:29 AM
 * @homepage : https://github.com/gusdnd852
 */
public class VolumeUpScenario {
    private static List<String> strings = Arrays.asList(
            "알았어요 소리를 키울게요",
            "볼륨을 키울게요",
            "네 소리를 키울게요"
    );

    public static void process(String speech, Activity activity) throws IOException {
        Brain.hippocampus.decideToSay(strings.get(Brain.random.nextInt(strings.size() - 1)));
        Volume vl = new Volume();
        vl.volumeUp(activity);
        Mouth.get().say(activity);
    }
}
