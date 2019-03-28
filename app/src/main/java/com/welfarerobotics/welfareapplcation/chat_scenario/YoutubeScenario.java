package com.welfarerobotics.welfareapplcation.chat_scenario;

import com.welfarerobotics.welfareapplcation.chat_api.YoutubeApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/26/2019 12:58 AM
 * @Homepage : https://github.com/gusdnd852
 */
public class YoutubeScenario {

    public static List<String> seperateEntity(String[][] entity) {
        String[] kewordGroup = entity[0];
        String[] entityGroup = entity[1];
        List<String> word = new ArrayList<>();

        for (int i = 0; i < entityGroup.length; i++) {
            if (entityGroup[i].contains("WORD")) {
                word.add(kewordGroup[i]);
            }
        }
        return word;
    }

    public static String response(List<String> word) throws IOException {
        if (word.size() != 0) {
            StringBuilder wordBuilder = new StringBuilder();
            for (String one : word) {
                wordBuilder.append(one);
            }

            return YoutubeApi.getYoutube(wordBuilder.toString());
        }
        return null;
    }
}
