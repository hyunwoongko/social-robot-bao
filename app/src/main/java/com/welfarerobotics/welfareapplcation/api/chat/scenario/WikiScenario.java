package com.welfarerobotics.welfareapplcation.api.chat.scenario;

import com.welfarerobotics.welfareapplcation.api.chat.CssApi;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.PreprocessorApi;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.WikiApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/25/2019 9:48 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class WikiScenario {

    private static String answer;

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
            CssApi.get().play(answer, "jinho");
            return true;
        }
        return false;
    }
}
