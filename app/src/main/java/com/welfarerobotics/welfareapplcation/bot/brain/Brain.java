package com.welfarerobotics.welfareapplcation.bot.brain;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.intent.IntentClassifier;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.preprocess.Preprocessor;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.state.ChatState;

import java.util.Random;

/**
 * @Author : Hyunwoong
 * @When : 3/23/2019 10:58 PM
 * @Homepage : https://github.com/gusdnd852
 * <p>
 * 뇌를 모방한 클래스
 * 모든 인공지능 처리의 중추
 * State Pattern 으로 구현됨
 */
public final class Brain {
    private static ChatState currentState = ChatState.NORMAL_STATE;
    public static Hippocampus hippocampus = new Hippocampus();
    public static Random random = new Random();
    private static String intent;


    public static void think(String speech , Activity activity) {
        try {
            System.out.println("입력 : " + speech);
            String preprocessedSpeech = Preprocessor.preprocess(speech);
            intent = IntentClassifier.classify(preprocessedSpeech);
            currentState = currentState.think(intent, preprocessedSpeech, activity);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void speech(Mouth mouth) {
        currentState = currentState.speech(mouth);
    }

    public static void draw() {
        // Generative Adversarial Nets
    }
}