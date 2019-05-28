package com.welfarerobotics.welfareapplcation.bot.brain.chat.state;

import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.Oblivion;
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

    @Override public ChatState think(String intent, String speech) throws IOException {
        if (intent == null) return FALLBACK_STATE.think(intent, speech);

        // 오늘은? 내일은 어때?  ||  일본어는? 미국은?  ||  전주는? 부산은? 대구는? ||
        // 그럼 미세먼지는? || 그럼 날씨는?
        if (intent.equals("날짜") || intent.equals("국가") || intent.equals("지역") ||
                (intent.equals("먼지") && Brain.hippocampus.getPreviousIntent().equals("날씨")) ||
                (intent.equals("날씨") && Brain.hippocampus.getPreviousIntent().equals("먼지")))
            return CONTEXT_STATE.think(intent, speech); // 문맥으로 전환

        // 단기기억이 필요 없는 의도들
        if (intent.equals("달력")) return CalenderScenario.process(speech);
        if (intent.equals("시간")) return TimeScenario.process(speech);
        if (intent.equals("명언")) return WiseScenario.process(speech);
        if (intent.equals("농담")) return JokeScenario.process(speech);
        if (intent.equals("동화")) return FairytaleScenario.process(speech);
        if (intent.equals("시선")) return SightScenario.process(speech);
        if (intent.equals("댄스")) return DanceScenario.process(speech);
        if (intent.equals("볼륨업")) return VolumeUpScenario.process(speech);
        if (intent.equals("볼륨다운")) return VolumeDownScenario.process(speech);
        if (intent.equals("와이파이")) return WifiScenario.process(speech);
        if (intent.equals("잘못들음")) return PardonScenario.process(speech);
        // 단기기억이 필요한 의도들
        if (intent.equals("먼지")) return DustScenario.process(speech, Oblivion::forgetAll);
        if (intent.equals("날씨")) return WeatherScenario.process(speech, Oblivion::forgetAll);
        if (intent.equals("맛집")) return RestaurantScenario.process(speech, Oblivion::forgetAll);
        if (intent.equals("위키")) return WikiScenario.process(speech, Oblivion::forgetAll);
        if (intent.equals("인물")) return WikiScenario.process(speech, Oblivion::forgetAll);
        if (intent.equals("뉴스")) return NewsScenario.process(speech, Oblivion::forgetAll);
        if (intent.equals("메모")) return AlramScenario.process(speech, Oblivion::forgetAll);
        if (intent.equals("알람")) return AlramScenario.process(speech, Oblivion::forgetAll);
        if (intent.equals("번역")) return TranslateScenario.process(speech, Oblivion::forgetAll);
        return OpenDomainScenario.process(intent, speech); // <- 오픈도메인 대화
    }

    @Override public ChatState speech(Mouth voice) {
        voice.play(Brain.hippocampus.getThoughtSentence());
        return this;
    }
}
