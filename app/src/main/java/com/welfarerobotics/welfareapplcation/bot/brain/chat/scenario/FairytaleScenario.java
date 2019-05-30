package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario;

import android.app.Activity;
import android.content.Intent;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.state.ChatState;
import com.welfarerobotics.welfareapplcation.core.fairytale.FairytaleActivity;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 2:23 AM
 * @homepage : https://github.com/gusdnd852
 */
public class FairytaleScenario {
    public static ChatState process(String speech, Activity activity) throws IOException {
        activity.startActivity(new Intent(activity, FairytaleActivity.class));
        return ChatState.NORMAL_STATE;
    }
}
