package com.welfarerobotics.welfareapplcation.bot.chat.scenario;

import com.welfarerobotics.welfareapplcation.bot.Voice;
import com.welfarerobotics.welfareapplcation.bot.chat.crawler.PreprocessorApi;
import com.welfarerobotics.welfareapplcation.bot.chat.crawler.WeatherApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/25/2019 7:53 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class WeatherScenario {

    private static String answer;

    public static String getAnswer() {
        return answer;
    }

    public static List<String>[] contextEntity(String[][] entity) {
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
        List<String>[] entites = new List[2];
        entites[0] = location;
        entites[1] = date;
        return entites;
    }

    public static List<String>[] seperateEntity(String[][] entity) {
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
        if (date.size() == 0) date.add("오늘");
        List<String>[] entites = new List[2];
        entites[0] = location;
        entites[1] = date;
        return entites;
    }

    public static boolean response(List<String> loc, List<String> date) throws IOException {
        if (loc.size() != 0) {
            StringBuilder locBuilder = new StringBuilder();
            for (String one : loc) {
                locBuilder.append(one);
            }

            StringBuilder dateBuilder = new StringBuilder();
            for (String one : date) {
                dateBuilder.append(one);
            }

            if (date.contains("오늘")) {
                answer = (PreprocessorApi.fix(WeatherApi.getTodayWeather(locBuilder.toString())));
                Voice.get().play(answer, "jinho");
            } else if (date.contains("모레") || date.contains("내일모레")) {
                answer = (PreprocessorApi.fix(WeatherApi.getAfterTomorrowWeather(locBuilder.toString())));
                Voice.get().play(answer, "jinho");
            } else if (date.contains("내일")) {
                answer = (PreprocessorApi.fix(WeatherApi.getTomorrowWeather(locBuilder.toString())));
                Voice.get().play(answer, "jinho");
            } else if (date.contains("이번") && date.contains("주")) {
                answer = (PreprocessorApi.fix(WeatherApi.getThisWeekWeather(locBuilder.toString())));
                Voice.get().play(answer, "jinho");
            } else {
                answer = (PreprocessorApi.fix(WeatherApi.getSpecificWeather(locBuilder.toString(), dateBuilder.toString())));
                Voice.get().play(answer, "jinho");
            }
            return true;
        }
        return false;
    }
}
