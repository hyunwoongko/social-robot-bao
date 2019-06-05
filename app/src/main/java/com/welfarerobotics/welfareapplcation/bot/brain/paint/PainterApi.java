package com.welfarerobotics.welfareapplcation.bot.brain.paint;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.Encoder;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/30/2019 9:58 AM
 * @homepage : https://github.com/gusdnd852
 */
public class PainterApi {

    public static String getPaint(String id, String text) throws IOException {
        return Jsoup.connect(ServerCache.getInstance().getPainter() + "/intent/" + Encoder
                .utf8(text))
                .timeout(20000)
                .get()
                .body()
                .text();
    }

    public static void draw() {

    }
}
