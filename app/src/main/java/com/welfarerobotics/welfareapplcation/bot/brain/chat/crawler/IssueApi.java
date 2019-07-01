package com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler;

import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 3/22/2019 2:31 PM
 * @Homepage : https://github.com/gusdnd852
 */
public final class IssueApi {
    /**
     * 네이버 이슈 API
     * 오늘의 이슈를 랜덤으로 5개 출력함
     *
     * @return 오늘의 이슈 5개
     */
    public static String getIssue() throws IOException {
        return Jsoup.connect(ServerCache.getInstance().getChat() + "/issue")
                .timeout(20000)
                .get()
                .body()
                .text();
    }
}
