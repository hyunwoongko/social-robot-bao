package com.welfarerobotics.welfareapplcation.bot.brain.chat.state;

import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.Oblivion;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.intent.ChatIntent;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.*;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/24/2019 4:59 PM
 * @homepage : https://github.com/gusdnd852
 */
public class NormalState implements ChatState {

    private static NormalState state;

    private NormalState() {
    }

    synchronized static NormalState getInstance() {
        if (state == null) state = new NormalState();
        return state;
    }

    @Override public ChatState think(ChatIntent intent, String preprocessedSpeech) throws IOException {
        if (intent == null) return FALLBACK_STATE.think(intent, preprocessedSpeech);
        String intentName = intent.getIntentName();

        if (intentName.equals("날짜") || intentName.equals("국가") || intentName.equals("지역"))
            return CONTEXT_STATE.think(intent, preprocessedSpeech); // 문맥으로 전환
            // 오늘은? 내일은 어때?  ||  일본어는? 미국은?  ||  전주는? 부산은? 대구는?

        else if (intentName.equals("달력")) return CalenderScenario.process(preprocessedSpeech);
        else if (intentName.equals("시간")) return TimeScenario.process(preprocessedSpeech);
        else if (intentName.equals("먼지")) return DustScenario.process(preprocessedSpeech);
        else if (intentName.equals("명언")) return WiseScenario.process(preprocessedSpeech);
        else if (intentName.equals("날씨")) return WeatherScenario.process(preprocessedSpeech, Oblivion::forgetAll);
        else if (intentName.equals("맛집")) return RestaurantScenario.process(preprocessedSpeech, Oblivion::forgetAll);
        else if (intentName.equals("위키")) return WikiScenario.process(preprocessedSpeech, Oblivion::forgetAll);
        else return OpenDomainScenario.process(intent, preprocessedSpeech); // <- 오픈도메인 대화
    }

    @Override public ChatState speech(Mouth voice) {
        voice.play(Brain.hippocampus.getThoughtSentence());
        return this;
    }
}
