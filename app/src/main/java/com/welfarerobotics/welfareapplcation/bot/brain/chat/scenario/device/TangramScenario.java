package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.device;

import android.app.Activity;
import android.content.Intent;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.core.contents.tangram.TangramActivity;
import com.welfarerobotics.welfareapplcation.core.settings.WifiActivity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/31/2019 12:56 PM
 * @homepage : https://github.com/gusdnd852
 */
public class TangramScenario {
    public static void process(String speech, Activity activity) throws IOException {
        activity.startActivity(new Intent(activity, TangramActivity.class));
    }
}
