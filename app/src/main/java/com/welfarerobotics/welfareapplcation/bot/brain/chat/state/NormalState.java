package com.welfarerobotics.welfareapplcation.bot.brain.chat.state;

import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.intent.ChatIntent;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.CalenderScenario;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.DustScenario;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.TimeScenario;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.WeatherScenario;

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

        else if (intentName.equals("달력")) return CalenderScenario.process(preprocessedSpeech);
        else if (intentName.equals("시간")) return TimeScenario.process(preprocessedSpeech);
        else if (intentName.equals("먼지")) return DustScenario.process(preprocessedSpeech);
        else if (intentName.equals("날씨")) return WeatherScenario.process(preprocessedSpeech);
        return FALLBACK_STATE.think(intent, preprocessedSpeech); // <- 오픈도메인 대화 구현
    }

    @Override public void speech(Mouth voice) {
        voice.play(Brain.hippocampus.getThoughtSentence());
    }
}
