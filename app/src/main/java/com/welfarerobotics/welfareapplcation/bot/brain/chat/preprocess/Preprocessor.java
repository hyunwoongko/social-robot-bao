package com.welfarerobotics.welfareapplcation.bot.brain.chat.preprocess;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.PreprocessorApi;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 5/12/2019 11:19 AM
 * @Homepage : https://github.com/gusdnd852
 * <p>
 * 전처리기
 */
public class Preprocessor {

    public static String preprocess(String speech) throws IOException {
        speech = PreprocessorApi.fix(speech);
        speech = PreprocessorApi.tokenize(speech);
        speech = PreprocessorApi.fix(speech);
        return speech;
    }


}
