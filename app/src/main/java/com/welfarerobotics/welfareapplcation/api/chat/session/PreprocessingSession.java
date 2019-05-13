package com.welfarerobotics.welfareapplcation.api.chat.session;

import com.welfarerobotics.welfareapplcation.api.chat.crawler.PreprocessorApi;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 5/12/2019 11:19 AM
 * @Homepage : https://github.com/gusdnd852
 */
public class PreprocessingSession {

    public static String preprocess(String speech) throws IOException {
        speech = PreprocessorApi.fix(speech);
        speech = PreprocessorApi.tokenize(speech);
        speech = PreprocessorApi.fix(speech);
        return speech;
    }
}
