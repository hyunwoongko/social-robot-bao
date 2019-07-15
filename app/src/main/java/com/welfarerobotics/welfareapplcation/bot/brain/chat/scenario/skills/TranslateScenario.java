package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.named_entity.TranslateEntityRecognizer;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.response.TranslateResponseGenerator;

import java.io.IOException;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 2:27 AM
 * @homepage : https://github.com/gusdnd852
 */
public class TranslateScenario {
    public static void process(String speech, Activity activity, Runnable... forgets) throws IOException {
        List<String>[] entities = TranslateEntityRecognizer.recognize(speech);
        for (Runnable forget : forgets) forget.run(); // 원하는 만큼 기억을 잊음.
        Brain.hippocampus.rememberTranslate(entities); // 해마에 저장
        String response = TranslateResponseGenerator.response();
        Brain.hippocampus.decideToSay(response);
        Mouth.get().say(activity);
    }
}
