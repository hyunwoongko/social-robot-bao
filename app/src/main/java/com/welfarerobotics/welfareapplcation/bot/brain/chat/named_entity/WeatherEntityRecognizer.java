package com.welfarerobotics.welfareapplcation.bot.brain.chat.named_entity;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.ModelApi;
import com.welfarerobotics.welfareapplcation.entity.cache.UserCache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/27/2019 3:16 PM
 * @homepage : https://github.com/gusdnd852
 */
public class WeatherEntityRecognizer {
    public static List<String>[] recognize(String preprocessedSpeech, boolean isContextMode) throws IOException {
        String[][] entity = ModelApi.getEntity("weather", preprocessedSpeech);
        String[] kewordGroup = entity[0];
        String[] entityGroup = entity[1];
        List<String> date = new ArrayList<>();
        List<String> location = new ArrayList<>();

        for (int i = 0; i < entityGroup.length; i++) {
            if (entityGroup[i].contains("DATE")) {
                date.add(kewordGroup[i]);
            } else if (entityGroup[i].contains("LOCATION")) {
                location.add(kewordGroup[i]);
            }
        }

        if (!isContextMode) { // 디폴트 모드 세팅
            if (date.size() == 0) date.add("오늘");
            if (location.size() == 0) location.add(UserCache.getInstance().getLocation());
        }

        List<String>[] entites = new List[2];
        entites[0] = location;
        entites[1] = date;
        return entites;
    }
}
