package com.welfarerobotics.welfareapplcation.bot.brain.chat.response;

import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.PreprocessorApi;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.RestaurantApi;

import java.io.IOException;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/27/2019 3:02 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class RestaurantResponseGenerator {


    public static String response() throws IOException {
        List<String> word = Brain.hippocampus.getLocation();

        if (word.size() != 0) {
            StringBuilder wordBuilder = new StringBuilder();
            for (String one : word) {
                wordBuilder.append(one);
            }
            return PreprocessorApi.fix(RestaurantApi.recommendRestaurant(wordBuilder.toString()));
        }
        return null;
    }
}
