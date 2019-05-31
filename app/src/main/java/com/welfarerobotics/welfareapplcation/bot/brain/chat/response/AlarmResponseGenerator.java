package com.welfarerobotics.welfareapplcation.bot.brain.chat.response;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.ModelApi;

import java.io.IOException;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/30/2019 10:14 AM
 * @homepage : https://github.com/gusdnd852
 */
public class AlarmResponseGenerator {
    public static List<String> recognize(String preprocessedSpeech) throws IOException {
        String[][] entity = ModelApi.getEntity("alarm", preprocessedSpeech);




        return null;
    }
}