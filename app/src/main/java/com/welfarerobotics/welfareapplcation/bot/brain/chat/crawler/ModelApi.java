package com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler;

import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
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
        return Jsoup.connect(ServerCache.getInstance().getChat() + "/intent/" + Encoder
                .utf8(text))
                .timeout(20000)
                .get()
                .body()
                .text();
    }


    /**
     * 오픈도메인 대화 메소드
     * 문장을 입력하면 대답을 출력함
     *
     * @param text 유저 입력 문장
     * @return 출력
     */
    public static String getOpenDomainAnswer(String text) throws IOException {
        return Jsoup.connect(ServerCache.getInstance().getChat() + "/open_domain/" + Encoder
                .utf8(text))
                .timeout(600000)
                .get()
                .body()
                .text();
    }


    /**
     * 감정지수(호르몬) 계산
     * 문장을 입력하면 호르몬 값을 출력함
     *
     * @param text 유저 입력 문장
     * @return 출력된 감정지수
     */
    public static float getEmotion(String text) throws IOException {
        return Float.valueOf(Jsoup.connect(ServerCache.getInstance().getChat() + "/emotion/" + Encoder
                .utf8(text))
                .timeout(600000)
                .get()
                .body()
                .text());
    }

    /**
     * Doc2Vec 유사도 파악 API
     * 문장을 입력하면 유사도에 따른 발화의도를 출력함
     *
     * @param text 발화 의도를 파악할 문장
     * @return 발화 의도
     */
    public static String getSimilarity(String text) throws IOException {
        return Jsoup.connect(ServerCache.getInstance().getChat() + "/similarity/" + Encoder
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
     * @return 단어와 개체명이 포함된 문자열배열
     */
    public static String[][] getEntity(String kind, String text) throws IOException {
        String entityString = Jsoup.connect(ServerCache.getInstance().getChat() + "/entity_" + kind + "/" + Encoder
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
}
