package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.context;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.brain.Oblivion;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.conversation.FallbackScenario;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills.DustScenario;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills.WeatherScenario;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/31/2019 2:42 PM
 * @homepage : https://github.com/gusdnd852
 */
public class LocationContextScenario {
    public static void process(String speech, String prevIntent, Activity activity) throws IOException {
        if (prevIntent.equals("날씨")) WeatherScenario.process(speech, activity,true, Oblivion::forgetLocation);
        else if (prevIntent.equals("먼지")) DustScenario.process(speech, activity,true, Oblivion::forgetLocation);
        else FallbackScenario.process(activity);
    }
}
