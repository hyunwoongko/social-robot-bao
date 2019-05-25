package com.welfarerobotics.welfareapplcation.api.chat.intent;

import com.welfarerobotics.welfareapplcation.api.chat.crawler.ModelApi;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 1:03 PM
 * @homepage : https://github.com/gusdnd852
 */
class CloseClassifier {

    static ChatIntent classify(String preprocessedSpeech) throws IOException {
        String intentName = ModelApi.getIntent(preprocessedSpeech);
        if (!intentName.equals("폴백")) {
            return ChatIntent.builder()
                    .intentName(intentName)
                    .build();
        } else return null;
    }
}
