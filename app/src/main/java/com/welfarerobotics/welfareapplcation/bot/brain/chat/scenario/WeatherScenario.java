package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario;

import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.named_entity.WeatherEntityRecognizer;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.response.WeatherResponseGenerator;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.state.ChatState;

import java.io.IOException;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/27/2019 6:01 PM
 * @homepage : https://github.com/gusdnd852
 */
public class WeatherScenario {
    public static ChatState process(String preprocessedSpeech) throws IOException {
        List<String>[] entities = WeatherEntityRecognizer.recognize(preprocessedSpeech, false);
        Brain.hippocampus.rememberWeather(entities); // 해마에 엔티티를 기억시킴.
        String response = WeatherResponseGenerator.response();
        Brain.hippocampus.decideToSay(response);
        return ChatState.NORMAL_STATE;
    }
}
