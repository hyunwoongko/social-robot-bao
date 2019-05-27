package com.welfarerobotics.welfareapplcation.bot.brain.chat.response;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.NewsApi;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.PreprocessorApi;

import java.io.IOException;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/27/2019 3:02 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class NewsResponseGenerator {

    private static String answer;

    public static String getAnswer() {
        return answer;
    }

    public static void response(List<String> word) throws IOException {
        if (word.size() != 0) {
            StringBuilder wordBuilder = new StringBuilder();
            for (String one : word) {
                wordBuilder.append(one);
            }
            answer = PreprocessorApi.fix(NewsApi.getKeywordNews(wordBuilder.toString()));
        } else {
            answer = PreprocessorApi.fix(NewsApi.getNews());
        }
    }
}
