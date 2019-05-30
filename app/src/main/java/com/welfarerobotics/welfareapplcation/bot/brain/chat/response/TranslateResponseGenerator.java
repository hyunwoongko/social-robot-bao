package com.welfarerobotics.welfareapplcation.bot.brain.chat.response;

import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.PreprocessorApi;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.TranslatorApi;

import java.io.IOException;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/25/2019 7:53 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class TranslateResponseGenerator {

    public static String response() throws IOException {
        List<String> lang = Brain.hippocampus.getLang();
        List<String> word = Brain.hippocampus.getWord();

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
                Brain.hippocampus.decideToSay(PreprocessorApi.fix(TranslatorApi.translate("ja", wordBuilder.toString())));
            } else if (lang.contains("영어") || lang.contains("영국어") || lang.contains("미국어") || lang.contains("미국말") || lang.contains("영국말")) {
                Brain.hippocampus.decideToSay(PreprocessorApi.fix(TranslatorApi.translate("en", wordBuilder.toString())));
            } else if (lang.contains("중국어") || lang.contains("중국") || lang.contains("중국말")) {
                Brain.hippocampus.decideToSay(PreprocessorApi.fix(TranslatorApi.translate("zh-CN", wordBuilder.toString())));
            } else if (lang.contains("스페인어") || lang.contains("스페인") || lang.contains("스페인말") || lang.contains("에스파냐어") || lang.contains("에스파뇰") || lang.contains("에스파냐말")) {
                Brain.hippocampus.decideToSay(PreprocessorApi.fix(TranslatorApi.translate("es", wordBuilder.toString())));
            } else if (lang.contains("한국어") || lang.contains("한국") || lang.contains("한국말") || lang.contains("우리말") || lang.contains("우리 나라말") || lang.contains("우리 말")) {
                Brain.hippocampus.decideToSay(PreprocessorApi.fix(TranslatorApi.translate("ko", wordBuilder.toString())));
            } else {
                Brain.hippocampus.decideToSay(PreprocessorApi.fix("아직 한국어, 영어, 중국어, 일본어, 스페인어로 밖에 말하지 못해요.  " + langBuilder.toString() + "은 조금 더 공부 해볼게요."));
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
