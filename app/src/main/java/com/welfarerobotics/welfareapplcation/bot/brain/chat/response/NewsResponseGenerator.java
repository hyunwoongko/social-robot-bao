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

    public static String response(List<String> word) throws IOException {
        if (word.size() != 0) {
            StringBuilder wordBuilder = new StringBuilder();
            for (String one : word) {
                wordBuilder.append(one);
            }
            return PreprocessorApi.fix(NewsApi.getKeywordNews(wordBuilder.toString()));
        } else {
            return PreprocessorApi.fix(NewsApi.getNews());
        }
    }
}
