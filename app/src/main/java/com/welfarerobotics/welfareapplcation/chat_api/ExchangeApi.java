package com.welfarerobotics.welfareapplcation.chat_api;

import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 3/27/2019 3:10 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class ExchangeApi {
    /**
     * 네이버 환율 API
     * 입력받은 국가의 환율을 출력함
     *
     * @param country 환율을 확인할 국가
     * @return 환율
     */
    public static String getExchange(String country) throws IOException {
        return Jsoup.connect(ApiServer.SERVER_URL + "/exchange/" + Encoder
                .utf8(country))
                .timeout(20000)
                .get()
                .body()
                .text();
    }
}
