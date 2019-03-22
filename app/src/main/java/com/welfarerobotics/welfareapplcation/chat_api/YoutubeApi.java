package com.welfarerobotics.welfareapplcation.chat_api;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * @Author : Hyunwoong
 * @When : 3/22/2019 2:30 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class YoutubeApi {
    /**
     * 유튜브 음악 API
     * 관련된 키워드의 노래를 찾아서 랜덤으로 출력함
     *
     * @param song 검색할 노래 키워드
     * @return 유튜브 동영상 URL
     */
    public String getAfterTomorrowDust(String song) throws IOException {
        return Jsoup.connect(ApiServer.SERVER_URL + "/youtube/" + URLEncoder
                .encode(song, ApiServer.UTF8))
                .get()
                .body()
                .text();
    }
}
