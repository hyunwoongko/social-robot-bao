package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.conversation;

import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.ModelApi;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 1:06 AM
 * @homepage : https://github.com/gusdnd852
 */
public class OpenDomainScenario {
    public static void process(String intent, String speech) throws IOException {
        String answer = ModelApi.getOpenDomainAnswer(speech);
        Brain.hippocampus.decideToSay(answer);
        Mouth.get().say();
    }
}
