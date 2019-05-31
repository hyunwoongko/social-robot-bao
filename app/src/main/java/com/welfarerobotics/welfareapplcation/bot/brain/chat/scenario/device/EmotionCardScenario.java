package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.device;

import android.app.Activity;
import android.content.Intent;
import com.welfarerobotics.welfareapplcation.core.contents.dictation.DictationActivity;
import com.welfarerobotics.welfareapplcation.core.contents.emotioncard.EmotioncardActivity;

/**
 * @author : Hyunwoong
 * @when : 5/31/2019 12:55 PM
 * @homepage : https://github.com/gusdnd852
 */
public class EmotionCardScenario {
    public static void process(String speech, Activity activity) {
        activity.startActivity(new Intent(activity, EmotioncardActivity.class));
    }
}
