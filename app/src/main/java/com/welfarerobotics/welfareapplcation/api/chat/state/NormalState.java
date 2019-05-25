package com.welfarerobotics.welfareapplcation.api.chat.state;

import com.welfarerobotics.welfareapplcation.api.chat.intent.ChatIntent;

/**
 * @author : Hyunwoong
 * @when : 5/24/2019 4:59 PM
 * @homepage : https://github.com/gusdnd852
 */
public class NormalState implements ChatState {

    private static NormalState state;

    private NormalState(){}
    synchronized static NormalState getInstance() {
        if (state == null) state = new NormalState();
        return state;
    }


    @Override public ChatState answer(ChatIntent intent) {

        if (intent.getIntentName().equals("날씨")) {

        } else if (intent.getIntentName().equals("먼지")) {

        }


        return null;
    }
}
