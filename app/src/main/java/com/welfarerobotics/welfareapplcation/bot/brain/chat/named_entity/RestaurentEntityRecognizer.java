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
public class RestaurentEntityRecognizer {


    private static String[] DO = {
            "경기도", "경기",
            "충청도", "충청",
            "충청남도", "충남",
            "충청북도", "충북",
            "전라도", "전라",
            "전라남도", "전남",
            "전라북도", "전북",
            "경상남도", "경남",
            "경상북도", "경북",
            "경상도", "경상",
            "강원도", "강원"
    };

    private static boolean isIn(String key) {
        boolean isIn = false;
        for (String DO : RestaurentEntityRecognizer.DO)
            if (key.contains(DO)) isIn = true;
        return isIn;
    }

    public static List<String> recognize(String preprocessedSpeech, boolean isContextMode) throws IOException {
        String[][] entity = ModelApi.getEntity("restaurant", preprocessedSpeech);
        String[] kewordGroup = entity[0];
        String[] entityGroup = entity[1];
        List<String> loc = new ArrayList<>();

        if (!isContextMode) { // 디폴트 모드 세팅
            if (loc.size() == 0) loc.add(UserCache.getInstance().getLocation());
        }
        for (int i = 0; i < entityGroup.length; i++) {
            if (entityGroup[i].contains("LOCATION")) {
                if (!isIn(kewordGroup[i])) {
                    loc.add(kewordGroup[i]);
                }
            }
        }
        return loc;
    }
}
