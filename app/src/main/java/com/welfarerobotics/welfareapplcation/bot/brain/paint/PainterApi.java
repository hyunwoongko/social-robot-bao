package com.welfarerobotics.welfareapplcation.bot.brain.paint;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.Encoder;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/30/2019 9:58 AM
 * @homepage : https://github.com/gusdnd852
 */
public class PainterApi {
    public static String getPaint(String uid, String path) throws IOException {
        Jsoup.connect(ServerCache.getInstance().getPainter() + "/random/" + uid + "/" + path)
                .timeout(20000)
                .ignoreContentType(true)
                .get();

        return ServerCache.getInstance().getPainter() + "/random/" + uid + "/" + path;
    }
}
