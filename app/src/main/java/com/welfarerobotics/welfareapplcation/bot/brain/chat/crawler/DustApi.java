package com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler;

import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 3/22/2019 2:31 PM
 * @Homepage : https://github.com/gusdnd852
 */
public final class DustApi {
    /**
     * 네이버 미세먼지 API - 오늘
     * 오늘의 지역별 미세먼지 상태를 출력함
     *
     * @param location 미세먼지 상태를 확인할 지역
     * @return 오늘의 지역별 미세먼지 상황
     */
    public static String getTodayDust(String location) throws IOException {
        return Jsoup.connect(ServerCache.getInstance().getChat() + "/today_dust/" + Encoder
                .utf8(location))
                .timeout(20000)
                .get()
                .body()
                .text();
    }

    /**
     * 네이버 미세먼지 API - 내일
     * 내일의 지역별 미세먼지 상태를 출력함
     *
     * @param location 미세먼지 상태를 확인할 지역
     * @return 내일의 지역별 미세먼지 상황
     */
    public static String getTomorrowDust(String location) throws IOException {
        return Jsoup.connect(ServerCache.getInstance().getChat() + "/tomorrow_dust/" + Encoder
                .utf8(location))
                .timeout(20000)
                .get()
                .body()
                .text();
    }

    /**
     * 네이버 미세먼지 API - 모레
     * 모레의 지역별 미세먼지 상태를 출력함
     *
     * @param location 미세먼지 상태를 확인할 지역
     * @return 모레의 지역별 미세먼지 상황
     */
    public static String getAfterTomorrowDust(String location) throws IOException {
        return Jsoup.connect(ServerCache.getInstance().getChat() + "/after_tomorrow_dust/" + Encoder
                .utf8(location))
                .timeout(20000)
                .get()
                .body()
                .text();
    }
}
