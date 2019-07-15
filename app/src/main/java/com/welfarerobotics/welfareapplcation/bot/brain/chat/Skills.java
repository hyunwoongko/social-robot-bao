package com.welfarerobotics.welfareapplcation.bot.brain.chat;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.Oblivion;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.context.ContextScenario;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.conversation.OpenDomainScenario;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.conversation.PardonScenario;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.device.*;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author : Hyunwoong
 * @when : 5/24/2019 4:59 PM
 * @homepage : https://github.com/gusdnd852
 */
public class Skills {

    public static void thinkAndSay(String intent, String speech, Activity activity) throws IOException, ExecutionException, InterruptedException {
        Brain.hippocampus.rememberIntent(intent);

        /** 문맥  모음 */
        if (intent.equals("날짜") || intent.equals("언어국가") || intent.equals("지역"))
            ContextScenario.process(speech, intent, activity);
        else if (intent.equals("알람")) AlarmScenario.process(speech, activity);
        else if (intent.equals("메모")) AlarmScenario.process(speech, activity);
        else if (intent.equals("달력")) CalenderScenario.process(speech, activity);
        else if (intent.equals("먼지")) DustScenario.process(speech, activity, false, Oblivion::forgetAll);
        else if (intent.equals("동화")) FairytaleScenario.process(speech, activity);
//        else if (intent.equals("이슈")) IssueScenario.process(speech, activity); 불안정해서 제외
//        else if (intent.equals("농담")) JokeScenario.process(speech, activity); 불안정해서 제외
        else if (intent.equals("뉴스")) NewsScenario.process(speech, activity, Oblivion::forgetAll);
        else if (intent.equals("맛집")) RestaurantScenario.process(speech, activity, Oblivion::forgetAll);
        else if (intent.equals("시간")) TimeScenario.process(speech, activity);
        else if (intent.equals("음악")) SongScenario.process(speech, activity);
        else if (intent.equals("번역")) TranslateScenario.process(speech, activity, Oblivion::forgetAll);
        else if (intent.equals("날씨")) WeatherScenario.process(speech, activity, false, Oblivion::forgetAll);
        else if (intent.equals("위키")) WikiScenario.process(speech, activity, Oblivion::forgetAll);
        else if (intent.equals("인물")) WikiScenario.process(speech, activity, Oblivion::forgetAll);
        else if (intent.equals("명언")) WiseScenario.process(speech, activity);

        /** 기기제어 */
        else if (intent.equals("볼륨업")) VolumeUpScenario.process(speech, activity);
        else if (intent.equals("볼륨다운")) VolumeDownScenario.process(speech, activity);
        else if (intent.equals("와이파이")) WifiScenario.process(speech, activity);
        else if (intent.equals("받아쓰기")) DictationScenario.process(speech, activity);
        else if (intent.equals("감정카드")) EmotionCardScenario.process(speech, activity);
        else if (intent.equals("낱말카드")) FlashCardScenario.process(speech, activity);
        else if (intent.equals("상식퀴즈")) QuizScenario.process(speech, activity);
        else if (intent.equals("조각맞추기")) TangramScenario.process(speech, activity);
        else if (intent.equals("그림")) PaintScenario.process(speech, activity);
        else if (intent.equals("메뉴")) MenuScenario.process(speech, activity);
        else if (intent.equals("게임")) MenuScenario.process(speech, activity);
        else if (intent.equals("설정")) SettingScenario.process(speech, activity);
        else OpenDomainScenario.process(intent, speech, activity); // <- 오픈도메인 대화
    }
}
