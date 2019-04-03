package com.welfarerobotics.welfareapplcation.api.chat.chatutil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 4/3/2019 11:38 AM
 * @Homepage : https://github.com/gusdnd852
 */
public final class ChatState {

    // 전처리 텍스트
    public static String tokenizeSpeech = "";
    public static String fixedSpeech = "";

    // 플래그
    public static boolean questionMode = false;
    public static boolean langMode = false;
    public static boolean wordMode = false;

    // 문맥 도구
    public static String contextIntent = "";
    public static String contextGeneratedAnswer = "";

    // 의도 및 개체명 인식 도구
    public static String intent;
    public static List<String> location = new ArrayList<>();
    public static List<String> date = new ArrayList<>();
    public static List<String> lang = new ArrayList<>();
    public static List<String> word = new ArrayList<>();


    public static void clear() {
        location.clear();
        date.clear();
        word.clear();
        lang.clear();
    }
}
