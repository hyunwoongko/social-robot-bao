package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.context;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.conversation.FallbackScenario;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills.DustScenario;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills.WeatherScenario;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/31/2019 12:57 PM
 * @homepage : https://github.com/gusdnd852
 */
public class ContextScenario {
    public static void process(String speech, String intent) throws IOException {
        String prevIntent = Brain.hippocampus.getPreviousIntent();
        if (intent.equals("날짜")) DateContextScenario.process(speech, prevIntent);
        if (intent.equals("언어국가")) LanguageContextScenario.process(speech);
        if (intent.equals("지역")) LocationContextScenario.process(speech, prevIntent);
        if (intent.equals("먼지")) WeatherScenario.process(speech);
        else if (intent.equals("날씨")) DustScenario.process(speech);
        else FallbackScenario.process();
    }
}
