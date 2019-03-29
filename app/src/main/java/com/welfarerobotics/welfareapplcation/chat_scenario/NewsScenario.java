package com.welfarerobotics.welfareapplcation.chat_scenario;

import com.welfarerobotics.welfareapplcation.chat_api.CssApi;
import com.welfarerobotics.welfareapplcation.chat_api.NewsApi;
import com.welfarerobotics.welfareapplcation.chat_api.PreprocessorApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/27/2019 3:02 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class NewsScenario {

    private static String answer;

    public static String getAnswer() {
        return answer;
    }

    public static List<String> seperateEntity(String[][] entity) {
        String[] kewordGroup = entity[0];
        String[] entityGroup = entity[1];
        List<String> word = new ArrayList<>();

        for (int i = 0; i < entityGroup.length; i++) {
            if (entityGroup[i].contains("WORD")) {
                word.add(kewordGroup[i]);
            }
        }
        return word;
    }

    public static void response(List<String> word) throws IOException {
        if (word.size() != 0) {
            StringBuilder wordBuilder = new StringBuilder();
            for (String one : word) {
                wordBuilder.append(one);
            }
            answer = PreprocessorApi.fix(NewsApi.getKeywordNews(wordBuilder.toString()));
            CssApi.get().play(answer, "jinho");
        } else {
            answer = PreprocessorApi.fix(NewsApi.getNews());
            CssApi.get().play(answer, "jinho");
        }
    }
}
