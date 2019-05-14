package com.welfarerobotics.welfareapplcation.api.chat.tools;

/**
 * @Author : Hyunwoong
 * @When : 4/3/2019 10:03 AM
 * @Homepage : https://github.com/gusdnd852
 */
public final class Emotion {

    private static String emotion = "";

    public static void setEmotion(String emotion) {
        Emotion.emotion = emotion;
    }

    public static String getEmotion() {
        return Emotion.emotion;
    }
}
