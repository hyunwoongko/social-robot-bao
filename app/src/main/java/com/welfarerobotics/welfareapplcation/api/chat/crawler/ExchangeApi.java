package com.welfarerobotics.welfareapplcation.api.chat.crawler;

import com.welfarerobotics.welfareapplcation.api.chat.chatutil.Encoder;
import com.welfarerobotics.welfareapplcation.entity.ServerCache;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 3/27/2019 3:10 PM
 * @Homepage : https://github.com/gusdnd852
 */
public final class ExchangeApi {
    /**
     * 네이버 환율 API
     * 입력받은 국가의 환율을 출력함
     *
     * @param country 환율을 확인할 국가
     * @return 환율
     */
    public static String getExchange(String country) throws IOException {
        return Jsoup.connect(ServerCache.getInstance().getUrl() + "/exchange/" + Encoder
                .utf8(country))
                .timeout(20000)
                .get()
                .body()
                .text();
    }
}
