package com.welfarerobotics.welfareapplcation.bot.brain.chat.intent;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 5/13/2019 8:13 PM
 * @Homepage : https://github.com/gusdnd852
 * <p>
 * Proxy Pattern 으로 구현
 */
public class IntentClassifier {

    public static ChatIntent classify(String preprocessedSpeech) throws IOException {
        ChatIntent intent = CloseClassifier.classify(preprocessedSpeech);
        if (intent != null) return intent;
        else intent = OpenClassifier.classify(preprocessedSpeech);
        return intent;
    }
}
