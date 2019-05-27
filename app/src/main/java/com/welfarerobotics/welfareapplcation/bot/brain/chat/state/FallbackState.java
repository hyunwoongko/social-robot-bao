package com.welfarerobotics.welfareapplcation.bot.brain.chat.state;

import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.intent.ChatIntent;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/27/2019 5:33 PM
 * @homepage : https://github.com/gusdnd852
 */
public class FallbackState implements ChatState {
    private static FallbackState state;

    private FallbackState() {
    }

    synchronized static FallbackState getInstance() {
        if (state == null) state = new FallbackState();
        return state;
    }

    @Override public ChatState think(ChatIntent intent, String preprocessedSpeech) throws IOException {
        return null;
    }

    @Override public void speech(Mouth voice) {

    }
}
