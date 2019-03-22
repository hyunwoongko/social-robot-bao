package com.welfarerobotics.welfareapplcation.chat_api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Author : Hyunwoong
 * @When : 3/22/2019 3:42 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class Encoder {

    /**
     * UTF-8 인코더
     * 공백을 + 가 아닌 " "으로 변경함
     *
     * @param text 인코딩할 문자
     * @return UTF-8 형식으로 인코딩된 문자
     */
    static String utf8(String text) throws UnsupportedEncodingException {
        return URLEncoder.encode(text, "UTF-8").replace("+", "%20");
    }
}
