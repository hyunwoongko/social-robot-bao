package com.welfarerobotics.welfareapplcation.chat_api;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/22/2019 9:48 AM
 * @Homepage : https://github.com/gusdnd852
 */
public final class ModelApi {
    /**
     * Char - CNN 발화의도 파악 API
     * 문장을 입력하면 발화의도를 출력함
     *
     * @param text 발화 의도를 파악할 문장
     * @return 발화 의도
     */
    public static String getIntent(String text) throws IOException {
        return Jsoup.connect(ApiServer.SERVER_URL + "/intent/" + Encoder
                .utf8(text))
                .timeout(20000)
                .get()
                .body()
                .text();
    }

    /**
     * BiLSTM - CRF 개체명 인식기 API
     * 문장을 입력하면 개체명을 인식함
     *
     * @param kind 개체 종류
     * @param text 개체명을 인식할 문장
     * @return 단어와 개체명이 포함된 Map
     */
    public static String[][] getEntity(String kind, String text) throws IOException {
        String entityString = Jsoup.connect(ApiServer.SERVER_URL + "/entity_" + kind + "/" + Encoder
                .utf8(text))
                .timeout(20000)
                .get()
                .body()
                .text();

        String[] splitedEntityString = entityString.split("\\[");
        List<String[]> splitEntityStringSet = new ArrayList<>();
        for (int i = 1; i < splitedEntityString.length; i++) {
            splitEntityStringSet.add(splitedEntityString[i]
                    .replace("\'", "")
                    .replace(",", "")
                    .replace("]", "")
                    .replace(")", "")
                    .split(" "));
        }

        return splitEntityStringSet.toArray(new String[0][]);
    }

    /**
     * Transformer 문장 생성기 API
     * 입력 문장을 입력하면 학습된 단어를 이어붙여 문장을 생성함
     *
     * @param userid 유저 아이디
     * @param text   입력문장
     * @return Transformer의 출력 문장
     */
    public static String generateAnswer(String userid, String text) throws IOException {
        return Jsoup.connect(ApiServer.SERVER_URL + "/generate_answer/" + userid + "/" + Encoder
                .utf8(text))
                .timeout(20000)
                .get()
                .body()
                .text();
    }
}
