package com.welfarerobotics.welfareapplcation.core.contents.dictation;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.Encoder;
import org.jsoup.Jsoup;

import java.io.IOException;

public class ImageCrawler {
    private String jsoup;

    public String crawler(String keyword) throws IOException {
        String google = "https://www.google.co.kr/search?q=";
        String image = "&tbm=isch";
        boolean ErrorFlag = true;
        int count = 0;

        while (ErrorFlag) {
            jsoup = Jsoup.connect(google + Encoder.utf8(keyword) + image)
                    .timeout(20000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
                    .get()
                    .select("div[class*=rg_meta]")
                    .get(count)
                    .html();

            try {
                int start = jsoup.indexOf("http");
                int end = jsoup.indexOf(".jpg");
                jsoup = jsoup.substring(start, end + 4);
                ErrorFlag = false;

            } catch (Exception e) {
                count++;
                ErrorFlag = true;
            }
        }
        return jsoup;
    }
}