package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.named_entity.NewsEntityRecognizer;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.response.NewsResponseGenerator;

import java.io.IOException;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 2:17 AM
 * @homepage : https://github.com/gusdnd852
 */
public class NewsScenario {
    public static void process(String speech, Activity activity, Runnable... forgets) throws IOException {
        List<String> entities = NewsEntityRecognizer.recognize(speech);
        for (Runnable forget : forgets) forget.run(); // 원하는 만큼 기억을 잊음.
        Brain.hippocampus.rememberWord(entities); // 해마에 엔티티를 기억시킴.
        String response = NewsResponseGenerator.response(entities);
        Brain.hippocampus.decideToSay(response);
        Mouth.get().say(activity);
    }
}
