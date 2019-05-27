package com.welfarerobotics.welfareapplcation.bot.brain.chat.intent;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.ModelApi;
import com.welfarerobotics.welfareapplcation.entity.Answer;
import com.welfarerobotics.welfareapplcation.entity.cache.ChatCache;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 1:03 PM
 * @homepage : https://github.com/gusdnd852
 */
public class OpenClassifier {
    static ChatIntent classify(String preprocessedSpeech) throws IOException {
        String intentName = ModelApi.getSimilarity(preprocessedSpeech);
        if (!intentName.equals("폴백")) {

            Answer answer = ChatCache.getInstance().getAnswer().get(intentName);
            if (answer != null) {
                return ChatIntent.builder()
                        .intentName(intentName)
                        .adrenalin(answer.getAdrenalin().getFigures())
                        .adrenalinAnswer(answer.getAdrenalin().getAnswer())
                        .cortisol(answer.getCortisol().getFigures())
                        .cortisolAnswer(answer.getCortisol().getAnswer())
                        .dopamine(answer.getDopamine().getFigures())
                        .dopamineAnswer(answer.getDopamine().getAnswer())
                        .endorphin(answer.getEndorphin().getFigures())
                        .endorphinAnswer(answer.getEndorphin().getAnswer())
                        .noradrenalin(answer.getNoradrenalin().getFigures())
                        .noradrenalinAnswer(answer.getNoradrenalin().getAnswer())
                        .serotonin(answer.getSerotonin().getFigures())
                        .serotoninAnswer(answer.getSerotonin().getAnswer())
                        .build();
            } else {
                return null;
            }
        } else return null;
    }
}
