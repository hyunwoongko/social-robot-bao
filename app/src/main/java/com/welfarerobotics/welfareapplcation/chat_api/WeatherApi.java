package com.welfarerobotics.welfareapplcation.chat_api;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URLEncoder;

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
    public String getTodayWeather(String location) throws IOException {
        return Jsoup.connect(ApiServer.SERVER_URL + "/today_weather/" + URLEncoder
                .encode(location, ApiServer.UTF8))
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
    public String getTomorrowWeather(String location) throws IOException {
        return Jsoup.connect(ApiServer.SERVER_URL + "/tomorrow_weather/" + URLEncoder
                .encode(location, ApiServer.UTF8))
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
    public String getAfterTomorrowWeather(String location) throws IOException {
        return Jsoup.connect(ApiServer.SERVER_URL + "/after_tomorrow_weather/" + URLEncoder
                .encode(location, ApiServer.UTF8))
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
    public String getThisWeekWeather(String location) throws IOException {
        return Jsoup.connect(ApiServer.SERVER_URL + "/this_week_weather/" + URLEncoder
                .encode(location, ApiServer.UTF8))
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
    public String getSpecificWeather(String location, String date) throws IOException {
        return Jsoup.connect(ApiServer.SERVER_URL + "/specific_weather/" + URLEncoder
                .encode(location, ApiServer.UTF8) + URLEncoder
                .encode(date, ApiServer.UTF8))
                .get()
                .body()
                .text();
    }
}
