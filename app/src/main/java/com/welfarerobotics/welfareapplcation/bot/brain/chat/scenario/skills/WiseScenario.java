package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.WiseApi;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 1:15 AM
 * @homepage : https://github.com/gusdnd852
 */
public class WiseScenario {
    public static void process(String speech, Activity activity) throws IOException {
        Brain.hippocampus.decideToSay(WiseApi.getWise());
        Mouth.get().say(activity);
    }
}
