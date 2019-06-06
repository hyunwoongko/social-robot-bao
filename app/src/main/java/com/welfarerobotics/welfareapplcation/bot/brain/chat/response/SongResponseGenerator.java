package com.welfarerobotics.welfareapplcation.bot.brain.chat.response;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.YoutubeApi;

import java.io.IOException;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/26/2019 12:58 AM
 * @Homepage : https://github.com/gusdnd852
 */
public class SongResponseGenerator {

    public static String response(List<String> word) throws IOException {
        StringBuilder wordBuilder = new StringBuilder();
        for (String one : word) {
            wordBuilder.append(one);
        }
        return YoutubeApi.getYoutube(wordBuilder.toString());
    }
}
