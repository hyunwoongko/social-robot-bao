package com.welfarerobotics.welfareapplcation.bot.brain.chat.state;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/24/2019 4:58 PM
 * @homepage : https://github.com/gusdnd852
 */
public class QuestionState implements ChatState {
    private static QuestionState state;

    private QuestionState() {
    }

    synchronized static QuestionState getInstance() {
        if (state == null) state = new QuestionState();
        return state;
    }

    @Override public ChatState think(String intent, String speech, Activity activity) throws IOException {
        intent = Brain.hippocampus.getPreviousIntent();
        if (intent.equals("맛집")) return null;
        if (intent.equals("번역")) return null;
        if (intent.equals("위키")) return null;
        if (intent.equals("알람")) return null;
        return null;
    }

    @Override public ChatState speech(Mouth voice) {

        return null;
    }
}
