package com.welfarerobotics.welfareapplcation.api.chat.session;

import com.welfarerobotics.welfareapplcation.api.chat.chatutil.ChatState;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.ModelApi;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.PreprocessorApi;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 4/3/2019 11:47 AM
 * @Homepage : https://github.com/gusdnd852
 *
 * 전처리 세션
 */
public final class PreprocessingSession {
    public static void preprocess(String speech) throws IOException {
        ChatState.fixedSpeech = PreprocessorApi.fix(speech);
        ChatState.tokenizeSpeech = PreprocessorApi.tokenize(ChatState.fixedSpeech);
        ChatState.tokenizeSpeech = PreprocessorApi.fix(ChatState.tokenizeSpeech);
    }
}
