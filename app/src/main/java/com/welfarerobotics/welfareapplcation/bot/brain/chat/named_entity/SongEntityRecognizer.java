package com.welfarerobotics.welfareapplcation.bot.brain.chat.named_entity;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.ModelApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/27/2019 3:18 PM
 * @homepage : https://github.com/gusdnd852
 */
public class SongEntityRecognizer {

    public static List<String> recognize(String processedSpeech) throws IOException {
        String[][] entity = ModelApi.getEntity("song", processedSpeech);
        String[] kewordGroup = entity[0];
        String[] entityGroup = entity[1];
        List<String> word = new ArrayList<>();

        for (int i = 0; i < entityGroup.length; i++) {
            if (entityGroup[i].contains("WORD")) {
                word.add(kewordGroup[i]);
            }
        }

        if (word.size() == 0)
            word.add("어린이 동요");

        return word;
    }
}
