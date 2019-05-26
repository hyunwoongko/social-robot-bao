package com.welfarerobotics.welfareapplcation.bot;

import android.support.v7.app.AppCompatActivity;
import com.welfarerobotics.welfareapplcation.bot.chat.intent.ChatIntent;
import com.welfarerobotics.welfareapplcation.bot.chat.intent.IntentClassifier;
import com.welfarerobotics.welfareapplcation.bot.chat.state.ChatState;
import com.welfarerobotics.welfareapplcation.bot.chat.preprocess.Preprocessor;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @Author : Hyunwoong
 * @When : 3/23/2019 10:58 PM
 * @Homepage : https://github.com/gusdnd852
 */
public final class Brain {
    private static Brain brain = null;

    private Brain() {
    }

    public synchronized static Brain get() {
        if (brain == null) brain = new Brain();
        return brain;
    }

    private ChatState currentState; // State 패턴 적용
    private ArrayList<ChatIntent> intentQueue = new ArrayList<>();
    private Pituitary pituitary = new Pituitary(); // 뇌하수체
    private Voice voice = Voice.get(); // 목소리

    public void think(String speech, AppCompatActivity activity) {
        try {
            String preprocessedSpeech = Preprocessor.preprocess(speech);
            ChatIntent intent = IntentClassifier.classify(preprocessedSpeech);
            System.out.println(intent.getIntentName());

            Voice.get().play(preprocessedSpeech, "jinho");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void talk() {

    }
}
