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

    @Override public ChatState think(ChatIntent intent, String speech) throws IOException {
        if (intent == null) return FALLBACK_STATE.think(intent, speech);
        String intentName = intent.getIntentName(); // 의도명 파악

        // 오늘은? 내일은 어때?  ||  일본어는? 미국은?  ||  전주는? 부산은? 대구는? // 미세먼지는? // 날씨는?
        if (intentName.equals("날짜") || intentName.equals("국가") || intentName.equals("지역") ||
                (intentName.equals("먼지") && Brain.hippocampus.getPreviousCloseDomainIntent().getIntentName().equals("날씨")) ||
                (intentName.equals("날씨") && Brain.hippocampus.getPreviousCloseDomainIntent().getIntentName().equals("먼지")))
            return CONTEXT_STATE.think(intent, speech); // 문맥으로 전환

        // 단기기억이 필요 없는 의도들
        if (intentName.equals("달력")) return CalenderScenario.process(speech);
        if (intentName.equals("시간")) return TimeScenario.process(speech);
        if (intentName.equals("명언")) return WiseScenario.process(speech);
        if (intentName.equals("농담")) return JokeScenario.process(speech);
        if (intentName.equals("동화")) return FairytaleScenario.process(speech);
        if (intentName.equals("시선")) return SightScenario.process(speech);
        if (intentName.equals("댄스")) return DanceScenario.process(speech);
        if (intentName.equals("볼륨업")) return VolumeUpScenario.process(speech);
        if (intentName.equals("볼륨다운")) return VolumeDownScenario.process(speech);
        if (intentName.equals("와이파이")) return WifiScenario.process(speech);
        if (intentName.equals("잘못들음")) return PardonScenario.process(speech);
        // 단기기억이 필요한 의도들
        if (intentName.equals("먼지")) return DustScenario.process(speech, Oblivion::forgetAll);
        if (intentName.equals("날씨")) return WeatherScenario.process(speech, Oblivion::forgetAll);
        if (intentName.equals("맛집")) return RestaurantScenario.process(speech, Oblivion::forgetAll);
        if (intentName.equals("위키")) return WikiScenario.process(speech, Oblivion::forgetAll);
        if (intentName.equals("인물")) return WikiScenario.process(speech, Oblivion::forgetAll);
        if (intentName.equals("뉴스")) return NewsScenario.process(speech, Oblivion::forgetAll);
        if (intentName.equals("메모")) return AlramScenario.process(speech, Oblivion::forgetAll);
        if (intentName.equals("알람")) return AlramScenario.process(speech, Oblivion::forgetAll);
        if (intentName.equals("번역")) return TranslateScenario.process(speech, Oblivion::forgetAll);
        return OpenDomainScenario.process(intent, speech); // <- 오픈도메인 대화
    }

    @Override public ChatState speech(Mouth voice) {
        voice.play(Brain.hippocampus.getThoughtSentence());
        return this;
    }
}
