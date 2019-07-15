package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.device;

import android.app.Activity;
import android.content.Intent;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.core.alarm.AlarmActivity;

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

    public static void process(String speech, Activity activity) throws IOException {
        Brain.hippocampus.decideToSay(strings.get(Brain.random.nextInt(strings.size() - 1)));
        Mouth.get().say(activity);
        activity.startActivity(new Intent(activity, AlarmActivity.class));
    }
}
