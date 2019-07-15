package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.named_entity.RestaurentEntityRecognizer;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.response.RestaurantResponseGenerator;

import java.io.IOException;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/27/2019 7:05 PM
 * @homepage : https://github.com/gusdnd852
 */
public class RestaurantScenario {
    public static void process(String speech, Activity activity, Runnable... forgets) throws IOException {
        List<String> entity = RestaurentEntityRecognizer.recognize(speech, false);
        for (Runnable forget : forgets) forget.run(); // 원하는 만큼 기억을 잊음.
        Brain.hippocampus.rememberLocation(entity);
        String res = RestaurantResponseGenerator.response();
        Brain.hippocampus.decideToSay(res);
        Mouth.get().say(activity);
    }
}
