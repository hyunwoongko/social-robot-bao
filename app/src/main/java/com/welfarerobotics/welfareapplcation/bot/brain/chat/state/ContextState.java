package com.welfarerobotics.welfareapplcation.bot.brain.chat.state;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.intent.ChatIntent;

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

    @Override public ChatState answer(ChatIntent intent, String preprocessedSpeech) {
        return null;
    }
}
