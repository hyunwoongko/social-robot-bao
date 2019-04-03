package com.welfarerobotics.welfareapplcation.api.chat.crawler;

import com.welfarerobotics.welfareapplcation.util.ApiKeys;
import com.welfarerobotics.welfareapplcation.api.chat.chatutil.Encoder;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 3/22/2019 2:26 PM
 * @Homepage : https://github.com/gusdnd852
 */


public final class WeatherApi {
    /**
     * 네이버 날씨 API - 오늘
     * 오늘의 지역별 날씨 상태를 출력함
     *
     * @param location 날씨 상태를 확인할 지역
     * @return 오늘의 지역별 날씨 상황
     */
    public static String getTodayWeather(String location) throws IOException {
        return Jsoup.connect(ApiKeys.SERVER_URL + "/today_weather/" + Encoder
                .utf8(location))
                .timeout(20000)
                .get()
                .body()
                .text();
    }

    /**
     * 네이버 날씨 API - 내일
     * 내일의 지역별 날씨 상태를 출력함
     *
     * @param location 날씨 상태를 확인할 지역
     * @return 내일의 지역별 날씨 상황
     */
    public static String getTomorrowWeather(String location) throws IOException {
        return Jsoup.connect(ApiKeys.SERVER_URL + "/tomorrow_weather/" + Encoder
                .utf8(location))
                .timeout(20000)
                .get()
                .body()
                .text();
    }

    /**
     * 네이버 날씨 API - 모레
     * 내일의 지역별 날씨 상태를 출력함
     *
     * @param location 날씨 상태를 확인할 지역
     * @return 모레의 지역별 날씨 상황
     */
    public static String getAfterTomorrowWeather(String location) throws IOException {
        return Jsoup.connect(ApiKeys.SERVER_URL + "/after_tomorrow_weather/" + Encoder
                .utf8(location))
                .timeout(20000)
                .get()
                .body()
                .text();
    }

    /**
     * 구글 날씨 API - 이번 주
     * 이번 주의 지역별 날씨 상태를 출력함
     *
     * @param location 날씨 상태를 확인할 지역
     * @return 이번 주의 지역별 날씨 상황
     */
    public static String getThisWeekWeather(String location) throws IOException {
        return Jsoup.connect(ApiKeys.SERVER_URL + "/this_week_weather/" + Encoder
                .utf8(location))
                .timeout(20000)
                .get()
                .body()
                .text();
    }

    /**
     * 구글 날씨 API - 특정 날짜
     * 특정 날짜의 지역별 날씨 상태를 출력함
     *
     * @param location 날씨 상태를 확인할 지역
     * @param date     날씨를 확인할 날짜
     * @return 특정 날짜의 지역별 날씨 상황
     */
    public static String getSpecificWeather(String location, String date) throws IOException {
        return Jsoup.connect(ApiKeys.SERVER_URL + "/specific_weather/" + Encoder
                .utf8(location) + "/" + Encoder
                .utf8(date))
                .timeout(20000)
                .get()
                .body()
                .text();
    }
}
