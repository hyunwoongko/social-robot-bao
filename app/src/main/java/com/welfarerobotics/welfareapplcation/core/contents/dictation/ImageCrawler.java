package com.welfarerobotics.welfareapplcation.core.contents.dictation;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.Encoder;
import org.jsoup.Jsoup;

import java.io.IOException;

public class ImageCrawler {

    public static String crawler(String keyword) throws IOException {
        String googleURL = "https://www.google.com/search?q=";
        String imageSearch = "&tbm=isch";
        return String.valueOf(Jsoup.connect(googleURL + Encoder
                .utf8(keyword) + imageSearch)
                .timeout(20000)
                .get()
                .select("data-ri=0 img src")
                .get(0));
    }
}