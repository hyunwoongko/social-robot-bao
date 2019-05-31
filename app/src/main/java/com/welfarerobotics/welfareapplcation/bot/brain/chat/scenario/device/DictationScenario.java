package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.device;

import android.app.Activity;
import android.content.Intent;
import com.welfarerobotics.welfareapplcation.core.contents.dictation.DictationActivity;
import com.welfarerobotics.welfareapplcation.core.contents.tangram.TangramActivity;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/31/2019 12:55 PM
 * @homepage : https://github.com/gusdnd852
 */
public class DictationScenario {
    public static void process(String speech, Activity activity) throws IOException {
        activity.startActivity(new Intent(activity, DictationActivity.class));
    }
}
