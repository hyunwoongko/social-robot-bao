package com.welfarerobotics.welfareapplcation.util.data_util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 1:21 PM
 * @homepage : https://github.com/gusdnd852
 */
public class UrlConverter {
    public static Bitmap convertUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // 서버로 부터 응답 수신
            conn.connect();

            InputStream is = conn.getInputStream(); // InputStream 값 가져오기
            // Bitmap으로 변환
            return BitmapFactory.decodeStream(is);
        } catch (Exception a) {
            System.out.println("URL CONVERTER : " + a);
            return null;
        }
    }
}
