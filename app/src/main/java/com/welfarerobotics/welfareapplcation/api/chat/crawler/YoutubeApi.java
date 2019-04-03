package com.welfarerobotics.welfareapplcation.api.chat.crawler;

import com.welfarerobotics.welfareapplcation.util.ApiKeys;
import com.welfarerobotics.welfareapplcation.api.chat.chatutil.Encoder;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 3/22/2019 2:30 PM
 * @Homepage : https://github.com/gusdnd852
 */
public final class YoutubeApi {
    /**
     * 유튜브 음악 API
     * 관련된 키워드의 노래를 찾아서 랜덤으로 출력함
     *
     * @param song 검색할 노래 키워드
     * @return 유튜브 동영상 URL
     */
    public static String getYoutube(String song) throws IOException {
        return Jsoup.connect(ApiKeys.SERVER_URL + "/youtube/" + Encoder
                .utf8(song))
                .timeout(20000)
                .get()
                .body()
                .text();
    }
}
