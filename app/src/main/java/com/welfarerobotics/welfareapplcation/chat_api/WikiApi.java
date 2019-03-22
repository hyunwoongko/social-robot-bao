package com.welfarerobotics.welfareapplcation.chat_api;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * @Author : Hyunwoong
 * @When : 3/22/2019 2:33 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class WikiApi {
    /**
     * 위키피디아 - 다음 국어사전 API
     * 문장을 입력하면 위키피디아를 검색하고,
     * 결과가 없으면 다음 국어사전 검색 결과를 출력함
     *
     * @param text 정보를 확인할 단어
     * @return 단어의 정보
     */
    public String getWiki(String text) throws IOException {
        return Jsoup.connect(ApiServer.SERVER_URL + "/wiki/" + URLEncoder
                .encode(text, ApiServer.UTF8))
                .get()
                .body()
                .text();
    }
}
