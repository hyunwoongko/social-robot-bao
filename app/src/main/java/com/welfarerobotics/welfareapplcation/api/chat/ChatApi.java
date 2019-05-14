package com.welfarerobotics.welfareapplcation.api.chat;

import android.support.v7.app.AppCompatActivity;
import com.welfarerobotics.welfareapplcation.api.chat.tools.ChatState;
import com.welfarerobotics.welfareapplcation.api.chat.tools.IntentClassifier;
import com.welfarerobotics.welfareapplcation.api.chat.tools.Preprocessor;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 3/23/2019 10:58 PM
 * @Homepage : https://github.com/gusdnd852
 */
public final class ChatApi {
    private static ChatApi api = null;

    private ChatApi() {
    }

    public synchronized static ChatApi get() {
        if (api == null) api = new ChatApi();
        return api;
    }

    public synchronized void chat(String speech, AppCompatActivity activity) {
        try {
            ChatState.speech = speech;
            ChatState.speech = Preprocessor.preprocess(speech);

            String intent = IntentClassifier.getIntent(speech);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
