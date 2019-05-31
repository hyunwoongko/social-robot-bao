package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.device;

import android.app.Activity;
import android.content.Intent;
import com.welfarerobotics.welfareapplcation.core.contents.tangram.TangramActivity;
import com.welfarerobotics.welfareapplcation.core.menu.MenuActivity;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/31/2019 12:56 PM
 * @homepage : https://github.com/gusdnd852
 */
public class MenuScenario {
    public static void process(String speech, Activity activity) throws IOException {
        activity.startActivity(new Intent(activity, MenuActivity.class));
    }
}
