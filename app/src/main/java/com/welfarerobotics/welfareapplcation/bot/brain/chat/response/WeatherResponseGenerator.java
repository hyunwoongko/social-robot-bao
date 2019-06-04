package com.welfarerobotics.welfareapplcation.bot.brain.chat.response;

import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.PreprocessorApi;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.WeatherApi;

import java.io.IOException;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/25/2019 7:53 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class WeatherResponseGenerator {

    public static String response() throws IOException {
        List<String> loc = Brain.hippocampus.getLocation();
        List<String> date = Brain.hippocampus.getDate();

        System.out.println("LOC : " + loc);
        System.out.println("DATE : " +date);

        StringBuilder locBuilder = new StringBuilder();
        for (String one : loc) {
            locBuilder.append(one);
        }

        StringBuilder dateBuilder = new StringBuilder();
        for (String one : date) {
            dateBuilder.append(one);
        }

        if (date.contains("오늘")) {
            return (PreprocessorApi.fix(WeatherApi.getTodayWeather(locBuilder.toString())));
        } else if (date.contains("모레") || date.contains("내일모레")) {
            return (PreprocessorApi.fix(WeatherApi.getAfterTomorrowWeather(locBuilder.toString())));
        } else if (date.contains("내일")) {
            return (PreprocessorApi.fix(WeatherApi.getTomorrowWeather(locBuilder.toString())));
        } else if (date.contains("이번") && date.contains("주")) {
            return (PreprocessorApi.fix(WeatherApi.getThisWeekWeather(locBuilder.toString())));
        } else {
            return (PreprocessorApi.fix(WeatherApi.getSpecificWeather(locBuilder.toString(), dateBuilder.toString())));
        }
    }
}
