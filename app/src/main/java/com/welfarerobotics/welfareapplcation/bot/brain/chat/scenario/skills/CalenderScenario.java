package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills;

import android.annotation.SuppressLint;
import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.Skills;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : Hyunwoong
 * @when : 5/27/2019 6:22 PM
 * @homepage : https://github.com/gusdnd852
 */
@SuppressLint("SimpleDateFormat")
public class CalenderScenario {
    public static void process(String speech, Activity activity) throws IOException {
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy년 MM월 dd일");
        Date time = new Date();
        String time2 = format2.format(time);
        Brain.hippocampus.decideToSay("오늘 날짜는 , " + time2 + " 입니다.");
        Mouth.get().say(activity);
    }
}
