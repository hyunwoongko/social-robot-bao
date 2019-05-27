package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario;

import android.annotation.SuppressLint;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.state.ChatState;

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
    public static ChatState process(String speech) throws IOException {
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy년 MM월 dd일");
        Date time = new Date();
        String time2 = format2.format(time);
        Brain.hippocampus.decideToSay("오늘 날짜는 " + time2 + " 이야");
        return ChatState.NORMAL_STATE;
    }
}
