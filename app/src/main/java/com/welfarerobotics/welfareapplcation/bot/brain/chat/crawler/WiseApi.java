package com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler;

import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 3/22/2019 2:32 PM
 * @Homepage : https://github.com/gusdnd852
 */
public final class WiseApi {
    /**
     * 네이버 명언 API
     * 랜덤으로 오늘의 명언을 출력함.
     *
     * @return 랜덤 명언
     */
    public static String getWise() throws IOException {
        return Jsoup.connect(ServerCache.getInstance().getChat() + "/wise")
                .timeout(20000)
                .get()
                .body()
                .text();
    }
}
