package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills;

import android.app.Activity;
import android.content.Intent;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.core.menu.MenuActivity;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 2:23 AM
 * @homepage : https://github.com/gusdnd852
 */
public class FairytaleScenario {
    public static void process(String speech, Activity activity) throws IOException {
        Intent intent = new Intent(activity, MenuActivity.class);
        intent.putExtra("key", "FairytaleScenario");
        activity.startActivity(intent);
        Brain.hippocampus.decideToSay("  ...  ");
    }
}
