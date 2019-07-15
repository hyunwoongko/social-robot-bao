package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.context;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.brain.Oblivion;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills.TranslateScenario;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/31/2019 2:41 PM
 * @homepage : https://github.com/gusdnd852
 */
public class LanguageContextScenario {
    public static void process(String speech, Activity activity) throws IOException {
        TranslateScenario.process(speech, activity, Oblivion::forgetLang);
    }
}
