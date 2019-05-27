package com.welfarerobotics.welfareapplcation.bot.brain.chat.response;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.PreprocessorApi;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.WikiApi;

import java.io.IOException;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/25/2019 9:48 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class WikiResponseGenerator {

    private static String answer;

    public static String getAnswer() {
        return answer;
    }

    public static boolean response(List<String> word) throws IOException {
        if (word.size() != 0) {
            StringBuilder wordBuilder = new StringBuilder();
            for (String one : word) {
                wordBuilder.append(one);
            }
            answer = PreprocessorApi.fix(WikiApi.getWiki(wordBuilder.toString()));
            return true;
        }
        return false;
    }
}
