package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario;

import android.annotation.SuppressLint;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.named_entity.WeatherEntityRecognizer;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.response.WeatherResponseGenerator;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.state.ChatState;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/27/2019 6:46 PM
 * @homepage : https://github.com/gusdnd852
 */
@SuppressLint("SimpleDateFormat")
public class TimeScenario {
    public static ChatState process(String speech) throws IOException {
        SimpleDateFormat format2 = new SimpleDateFormat("HH시 mm분 ss초");
        Date time = new Date();
        String time2 = format2.format(time);
        Brain.hippocampus.decideToSay("지금 시간은 , " + time2 + " 입니다.");
        return ChatState.NORMAL_STATE;
    }
}
