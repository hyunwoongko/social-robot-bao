package com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler;

import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 3/27/2019 3:10 PM
 * @Homepage : https://github.com/gusdnd852
 */
public final class NewsApi {
    /**
     * 네이버 뉴스 API - 주요뉴스
     * 현재 주요 뉴스를 출력함
     *
     * @return 현재 주요 뉴스
     */
    public static String getNews() throws IOException {
        return Jsoup.connect(ServerCache.getInstance().getChat() + "/news/")
                .timeout(20000)
                .get()
                .body()
                .text();
    }

    /**
     * 네이버 뉴스 API - 키워드
     * 입력받은 키워드의 뉴스를 검색하여 출력함
     *
     * @param keyword 뉴스 키워드
     * @return 키워드별로 추천된 뉴스
     */
    public static String getKeywordNews(String keyword) throws IOException {
        return Jsoup.connect(ServerCache.getInstance().getChat() + "/keyword_news/" + Encoder
                .utf8(keyword))
                .timeout(20000)
                .get()
                .body()
                .text();
    }
}
