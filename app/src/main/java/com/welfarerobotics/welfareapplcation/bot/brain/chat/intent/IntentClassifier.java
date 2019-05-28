package com.welfarerobotics.welfareapplcation.bot.brain.chat.intent;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.ModelApi;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 5/13/2019 8:13 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class IntentClassifier {

    public static String classify(String preprocessedSpeech) throws IOException {
        return ModelApi.getIntent(preprocessedSpeech);
    }
}
