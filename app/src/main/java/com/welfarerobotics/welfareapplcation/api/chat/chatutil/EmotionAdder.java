package com.welfarerobotics.welfareapplcation.api.chat.chatutil;

import com.welfarerobotics.welfareapplcation.util.RandomModule;

/**
 * @Author : Hyunwoong
 * @When : 4/3/2019 10:03 AM
 * @Homepage : https://github.com/gusdnd852
 */
public final class EmotionAdder {

    private static String emotion = "";

    //감정 배열
    private static String[] happiness = {
            " , 그런데 오늘따라 기분이 좋아보이세요.",
            " , 그런데 표정을 보니 기분이 좋아보이시네요.",
            " , 근데 오늘 왠지 기분이 좋아보이시는데요.",
            " , 근데 혹시 오늘 좋은일 있으신가요? , 기분이 좋아보여요",
            " , 그런데 지금 기분이 좋아보이시네요.",
            " , 그런데 방금 표정을 보니 오늘 좋은일이 있으신 것 같아요. , 표정이 밝으시네요"};

    private static String[] surprise = {
            " , 그런데 무슨 일 있으신가요? , 놀란 표정을 지으셔서요.",
            " , 그런데 표정을 보니 놀라보이세요. , 무슨일 있으신가요?",
            " , 근데 지금 표정을 보니 무언가 놀라신것 같아요, , 무슨 일 있으세요?",
            " , 근데 혹시 오늘 무슨일 있으신가요? , 놀라시는 표정을 지으셔서요.",
            " , 그런데 방금 놀란 표정을 지으신것 같네요. 무슨 일 있으신가요?",
            " , 그런데 방금 뭐가 지나갔나요? , 상당히 놀란 표정을 지으셔서요."};

    public static String applyEmotion(String input) {
        int prob = RandomModule.random.nextInt(3);
        // 0, 1, 2 중 하나의 수가 출력됨

        if (prob == 1) { // 1일경우, 즉 1/3 확률로 감정 텍스트를 출력함
            if (emotion.equals("happiness")) {
                return input + happiness[RandomModule.random.nextInt(happiness.length)];
            } else if (emotion.equals("surprise")) {
                return input + surprise[RandomModule.random.nextInt(surprise.length)];
            }
        }
        return input;
    }

    public static void setEmotion(String emotion) {
        EmotionAdder.emotion = emotion;
    }
    public static String getEmotion() {
        return  EmotionAdder.emotion;
    }
}
