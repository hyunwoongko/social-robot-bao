package com.welfarerobotics.welfareapplcation.api.chat;

import android.support.v7.app.AppCompatActivity;
import com.welfarerobotics.welfareapplcation.api.chat.intent.ChatIntent;
import com.welfarerobotics.welfareapplcation.api.chat.intent.IntentClassifier;
import com.welfarerobotics.welfareapplcation.api.chat.state.ChatState;
import com.welfarerobotics.welfareapplcation.api.chat.tools.Preprocessor;

import java.io.IOException;
import java.util.ArrayList;

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

    private ChatState currentState; // State 패턴 적용
    private ArrayList<ChatIntent> intentQueue = new ArrayList<>();

    public synchronized void chat(String speech, AppCompatActivity activity) {
        try {
            String preprocessedSpeech = Preprocessor.preprocess(speech);
            ChatIntent intent = IntentClassifier.classify(preprocessedSpeech);
            System.out.println(intent.getIntentName());

            CssApi.get().play(preprocessedSpeech, "jinho");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
