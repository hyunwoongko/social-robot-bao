package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.device;

import android.app.Activity;
import android.content.Intent;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.core.settings.WifiActivity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 2:29 AM
 * @homepage : https://github.com/gusdnd852
 */
public class WifiScenario {
    private static List<String> strings = Arrays.asList(
            "와이파이 설정으로 이동합니다",
            "와이파이 설정 화면으로 이동합니다."
    );

    public static void process(String speech, Activity activity) throws IOException {
        Brain.hippocampus.decideToSay(strings.get(Brain.random.nextInt(strings.size() - 1)));
        Mouth.get().say(activity);
        activity.startActivity(new Intent(activity, WifiActivity.class));
    }
}
