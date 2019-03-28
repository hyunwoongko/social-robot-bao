package com.welfarerobotics.welfareapplcation.chat_scenario;

import com.welfarerobotics.welfareapplcation.chat_api.PreprocessorApi;
import com.welfarerobotics.welfareapplcation.chat_api.TranslatorApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/25/2019 7:53 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class TranslateScenario {

    private static String answer = "";

    public static String getAnswer() {
        return answer;
    }

    public static List<String>[] seperateEntity(String[][] entity) {
        String[] kewordGroup = entity[0];
        String[] entityGroup = entity[1];
        List<String> word = new ArrayList<>();
        List<String> lang = new ArrayList<>();

        for (int i = 0; i < entityGroup.length; i++) {
            if (entityGroup[i].contains("WORD")) {
                word.add(kewordGroup[i]);
            } else if (entityGroup[i].contains("LANG")) {
                lang.add(kewordGroup[i]);
            }
        }
        List<String>[] entites = new List[2];
        entites[0] = word;
        entites[1] = lang;
        return entites;
    }

    public static String response(List<String> word, List<String> lang) throws IOException {
        if (lang.size() != 0 && word.size() != 0) {
            StringBuilder wordBuilder = new StringBuilder();
            for (String one : word) {
                wordBuilder.append(one);
            }
            StringBuilder langBuilder = new StringBuilder();
            for (String one : lang) {
                langBuilder.append(one);
            }

            if (lang.contains("일본") || lang.contains("일본어") || lang.contains("일본말")) {
                answer = PreprocessorApi.fix(TranslatorApi.translate("ja", wordBuilder.toString()));
                CssApi.get().play(answer, "shinji");
            } else if (lang.contains("영어") || lang.contains("영국어") || lang.contains("미국어") || lang.contains("미국말") || lang.contains("영국말")) {
                answer = PreprocessorApi.fix(TranslatorApi.translate("en", wordBuilder.toString()));
                CssApi.get().play(answer, "matt");
            } else if (lang.contains("중국어") || lang.contains("중국") || lang.contains("중국말")) {
                answer = PreprocessorApi.fix(TranslatorApi.translate("zh-CN", wordBuilder.toString()));
                CssApi.get().play(answer, "liangliang");
            } else if (lang.contains("스페인어") || lang.contains("스페인") || lang.contains("스페인말") || lang.contains("에스파냐어") || lang.contains("에스파뇰") || lang.contains("에스파냐말")) {
                answer = PreprocessorApi.fix(TranslatorApi.translate("es", wordBuilder.toString()));
                CssApi.get().play(answer, "jose");
            }else if (lang.contains("한국어") || lang.contains("한국") || lang.contains("한국말") || lang.contains("우리말")|| lang.contains("우리 나라말") || lang.contains("우리 말")) {
                answer = PreprocessorApi.fix(TranslatorApi.translate("ko", wordBuilder.toString()));
                CssApi.get().play(answer, "jinho");
            } else {
                answer = PreprocessorApi.fix("아직 한국어, 영어, 중국어, 일본어, 스페인어로 밖에 말하지 못해요.  " + langBuilder.toString() + "은 조금 더 공부 해볼게요.");
                CssApi.get().play(answer, "jinho");
            }
            return "good";
        } else if (lang.size() == 0 && word.size() != 0) {
            return "no lang";
        } else if (word.size() == 0 && lang.size() != 0) {
            return "no word";
        } else {
            return "no lang word";
        }
    }
}
