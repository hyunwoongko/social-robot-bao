package com.welfarerobotics.welfareapplcation.bot.brain.chat.state;

import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.Oblivion;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.intent.ChatIntent;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.DustScenario;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.RestaurantScenario;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.TranslateScenario;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.WeatherScenario;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/24/2019 5:01 PM
 * @homepage : https://github.com/gusdnd852
 */
public class ContextState implements ChatState {
    private static ContextState state;

    private ContextState() {
    }

    public static ContextState getInstance() {
        if (state == null) state = new ContextState();
        return state;
    }

    @Override public ChatState think(ChatIntent intent, String speech) throws IOException {
        String prevIntent = Brain.hippocampus.getPreviousCloseDomainIntent().getIntentName();
        String currentIntent = intent.getIntentName();
        if (currentIntent.equals("날짜")) { // 내일은? || 오늘은? || 모레는?
            if (prevIntent.equals("먼지")) return DustScenario.process(speech, Oblivion::forgetDate);
            if (prevIntent.equals("날씨")) return WeatherScenario.process(speech, Oblivion::forgetDate);
        }
        if (currentIntent.equals("지역")) { // 전주는? || 부산은? || 대구는 어때?
            if (prevIntent.equals("먼지")) return DustScenario.process(speech, Oblivion::forgetLocation);
            if (prevIntent.equals("날씨")) return WeatherScenario.process(speech, Oblivion::forgetLocation);
            if (prevIntent.equals("맛집")) return RestaurantScenario.process(speech, Oblivion::forgetLocation);
        }
        if (currentIntent.equals("국가")) { // 미국은? || 일본어는? || 독일어는?
            if (prevIntent.equals("번역"))
                return TranslateScenario.process(Brain.hippocampus.getThoughtSentence(), Oblivion::forgetLang);
        }

        // 날씨 <-> 먼지 간의 의도 변환
        if (currentIntent.equals("날씨") && prevIntent.equals("먼지")) return WeatherScenario.process(speech);
        if (currentIntent.equals("먼지") && prevIntent.equals("날씨")) return DustScenario.process(speech);
        return this;
    }

    @Override public ChatState speech(Mouth mouth) {
        mouth.play(Brain.hippocampus.getThoughtSentence());
        return NORMAL_STATE;
    }
}
