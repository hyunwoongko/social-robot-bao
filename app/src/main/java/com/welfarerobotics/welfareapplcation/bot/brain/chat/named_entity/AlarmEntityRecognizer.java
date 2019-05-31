package com.welfarerobotics.welfareapplcation.bot.brain.chat.named_entity;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.ModelApi;
import com.welfarerobotics.welfareapplcation.entity.Alarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/30/2019 10:14 AM
 * @homepage : https://github.com/gusdnd852
 */
public class AlarmEntityRecognizer {
    public static Alarm recognize(String preprocessedSpeech) throws IOException {
        String[][] entity = ModelApi.getEntity("alarm", preprocessedSpeech);
        String[] kewordGroup = entity[0];
        String[] entityGroup = entity[1];
        Alarm alarm = new Alarm();

        for (int i = 0; i < entityGroup.length; i++) {
            if (entityGroup[i].contains("POST")) {
                alarm.getPOST().add(kewordGroup[i]);
            } else if (entityGroup[i].contains("DATE")) {
                alarm.getDATE().add(kewordGroup[i]);
            } else if (entityGroup[i].contains("MONTH")) {
                alarm.getMONTH().add(kewordGroup[i]);
            } else if (entityGroup[i].contains("PRE")) {
                alarm.getPRE().add(kewordGroup[i]);
            } else if (entityGroup[i].contains("WEEK")) {
                alarm.getWEEK().add(kewordGroup[i]);
            } else if (entityGroup[i].contains("HOUR")) {
                alarm.getHOUR().add(kewordGroup[i]);
            } else if (entityGroup[i].contains("TIMEZONE")) {
                alarm.getTIMEZONE().add(kewordGroup[i]);
            } else if (entityGroup[i].contains("DAY")) {
                alarm.getDAY().add(kewordGroup[i]);
            } else if (entityGroup[i].contains("MIN")) {
                alarm.getMIN().add(kewordGroup[i]);
            } else if (entityGroup[i].contains("EVERY")) {
                alarm.getEVERY().add(kewordGroup[i]);
            } else if (entityGroup[i].contains("HALF")) {
                alarm.getHALF().add(kewordGroup[i]);
            }
        }
        return alarm;
    }
}
