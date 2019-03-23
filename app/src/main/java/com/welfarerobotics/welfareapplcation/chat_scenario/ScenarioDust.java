package com.welfarerobotics.welfareapplcation.chat_scenario;

import com.welfarerobotics.welfareapplcation.chat_api.DustApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/22/2019 4:30 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class ScenarioDust {
    public String response(String[][] entity) throws IOException {
        String[] kewordGroup = entity[0];
        String[] entityGroup = entity[1];
        List<String> date = new ArrayList<>();
        List<String> location = new ArrayList<>();
        DustApi api = new DustApi();


        for (int i = 0; i < entityGroup.length; i++) {
            if (entityGroup[i].contains("DATE")) {
                date.add(kewordGroup[i]);
            } else if (entityGroup[i].contains("LOCATION")) {
                location.add(kewordGroup[i]);
            }
        }

        if (date.size() == 0) date.add("오늘");
        if (location.size() == 0) {
            while (location.size() == 0) {
                Voice.tts("어느 지역을 알려드릴까요?");
                String loc = Voice.stt();
                if (loc != null && !loc.trim().equals("")) {
                    location.add(loc);
                }
            }
        }

        StringBuilder loc = new StringBuilder();
        for (String one : location) {
            loc.append(one);
        }

        if (date.contains("오늘")) {
            return api.getTodayDust(loc.toString());
        } else if (date.contains("모레") || date.contains("내일모레")) {
            return api.getAfterTomorrowDust(loc.toString());
        } else if (date.contains("내일")) {
            return api.getTomorrowDust(loc.toString());
        } else {
            return "오늘, 내일, 모레의 공기 상태만 알 수 있어요";
        }
    }
}