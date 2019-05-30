package com.welfarerobotics.welfareapplcation.bot.brain.chat.named_entity;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.ModelApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/27/2019 3:44 PM
 * @homepage : https://github.com/gusdnd852
 */
public class TranslateEntityRecognizer {
    public static List<String>[] recognize(String preprocessedSpeech) throws IOException {
        String[][] entity = ModelApi.getEntity("translate", preprocessedSpeech);
        String[] kewordGroup = entity[0];
        String[] entityGroup = entity[1];
        List<String> word = new ArrayList<>();
        List<String> lang = new ArrayList<>();

        for (int i = 0; i < entityGroup.length; i++) {
            if (entityGroup[i].contains("WORD")) {
                word.add(kewordGroup[i]);
            } else if (entityGroup[i].contains("LANG")) {
                lang.add(kewordGroup[i]);
            }
        }
        List<String>[] entites = new List[2];
        entites[0] = word;
        entites[1] = lang;
        return entites;
    }
}