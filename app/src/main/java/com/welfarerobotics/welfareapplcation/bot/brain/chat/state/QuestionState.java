package com.welfarerobotics.welfareapplcation.bot.brain.chat.state;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.intent.ChatIntent;

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


    @Override public ChatState answer(ChatIntent intent) {
        return null;
    }
}
