package com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler;

import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 3/27/2019 3:09 PM
 * @Homepage : https://github.com/gusdnd852
 */
public final class RestaurantApi {
    /**
     * 네이버 맛집 API
     * 입력받은 키워드의 맛집을 검색하여 추천함
     *
     * @param keyword 맛집 키워드
     * @return 맛집 추천
     */
    public static String recommendRestaurant(String keyword) throws IOException {
        return Jsoup.connect(ServerCache.getInstance().getChat() + "/restaurant/" + Encoder
                .utf8(keyword))
                .timeout(20000)
                .get()
                .body()
                .text();
    }
}
