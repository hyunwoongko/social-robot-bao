package com.welfarerobotics.welfareapplcation.api.chat.scenario;

import com.welfarerobotics.welfareapplcation.api.chat.CssApi;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.ExchangeApi;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.PreprocessorApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/27/2019 3:01 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class ExchangeScenario {
    private static String answer;

    public static String getAnswer() {
        return answer;
    }

    public static List<String> seperateEntity(String[][] entity) {
        String[] kewordGroup = entity[0];
        String[] entityGroup = entity[1];
        List<String> coun = new ArrayList<>();

        for (int i = 0; i < entityGroup.length; i++) {
            if (entityGroup[i].contains("COUN")) {
                coun.add(kewordGroup[i]);
            }
        }
        return coun;
    }


    public static boolean response(List<String> word) throws IOException {
        if (word.size() != 0) {
            StringBuilder wordBuilder = new StringBuilder();
            for (String one : word) {
                wordBuilder.append(one);
            }
            answer = PreprocessorApi.fix(ExchangeApi.getExchange(wordBuilder.toString()));
            CssApi.get().play(answer, "jinho");
            return true;
        }
        return false;
    }
}
