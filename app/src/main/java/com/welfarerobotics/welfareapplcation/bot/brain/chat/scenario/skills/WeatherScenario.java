package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.named_entity.WeatherEntityRecognizer;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.response.WeatherResponseGenerator;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/27/2019 6:01 PM
 * @homepage : https://github.com/gusdnd852
 */
public class WeatherScenario {
    public static void process(String speech, Activity activity, boolean contextMode, Runnable... forgets) throws IOException {
        List<String>[] entities = WeatherEntityRecognizer.recognize(speech, contextMode);
        System.out.println(Arrays.toString(entities));
        for (Runnable forget : forgets) forget.run(); // 원하는 만큼 기억을 잊음.
        Brain.hippocampus.rememberWeather(entities); // 해마에 엔티티를 기억시킴.
        String response = WeatherResponseGenerator.response();
        Brain.hippocampus.decideToSay(response);
        Mouth.get().say(activity);
    }
}
