package com.welfarerobotics.welfareapplcation.bot.brain.chat.response;

import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.DustApi;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.PreprocessorApi;

import java.io.IOException;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/23/2019 11:54 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class DustResponseGenerator {

    public static String response() throws IOException {
        List<String> loc = Brain.hippocampus.getLocation();
        List<String> date = Brain.hippocampus.getDate();
        StringBuilder locBuilder = new StringBuilder();
        for (String one : loc) {
            locBuilder.append(one);
        }

        StringBuilder dateBuilder = new StringBuilder();
        for (String one : date) {
            dateBuilder.append(one);
            dateBuilder.append(" ");
        }

        if (date.contains("오늘")) {
            return PreprocessorApi.fix(DustApi.getTodayDust(locBuilder.toString()));
        } else if (date.contains("모레") || date.contains("내일모레")) {
            return PreprocessorApi.fix(DustApi.getAfterTomorrowDust(locBuilder.toString()));
        } else if (date.contains("내일")) {
            return PreprocessorApi.fix(DustApi.getTomorrowDust(locBuilder.toString()));
        } else {
            return "오늘, 내일, 모레의 미세먼지 상태만 알 수 있어요";
        }
    }
}
