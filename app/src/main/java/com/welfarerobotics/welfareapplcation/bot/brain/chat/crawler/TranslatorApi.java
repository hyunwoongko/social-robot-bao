package com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler;

import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 3/22/2019 2:32 PM
 * @Homepage : https://github.com/gusdnd852
 */
public final class TranslatorApi {

    /**
     * 구글 번역 API
     * 입력받은 텍스트를 해당 언어로 번역함
     *
     * @param lang 번역할 언어
     * @param text 번역할 문장
     * @return 해당 언어로 번역된 문장
     */
    public static String translate(String lang, String text) throws IOException {
        return Jsoup.connect(ServerCache.getInstance().getChat() + "/translate/" + Encoder
                .utf8(lang) + "/" + Encoder
                .utf8(text))
                .timeout(20000)
                .get()
                .body()
                .text();
    }
}
