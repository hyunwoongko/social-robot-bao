package com.welfarerobotics.welfareapplcation.bot.brain;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.ModelApi;

import java.io.IOException;
import java.util.LinkedList;

/**
 * @author : Hyunwoong
 * @when : 6/16/2019 4:18 AM
 * @homepage : https://github.com/gusdnd852
 * <p>
 * 현재 호르몬 상태를 관장하는 클래스
 */
public class Pituitary {
    private static LinkedList<Float> hormones = new LinkedList<>();  // 선입 선출을 위해서 연결리스트 사용
    private static float currentEmotion = 0.0f; // 현재 감정상태를 저장하고 있는 변수

    /**
     * 새로운 말을 Queue 에 저장하는 메소드
     * # 이 달린경우는 욕설로 판단하여 호르몬을 -1 로 지정
     *
     * @param userText 사용자의 입력
     */
    public static Hormone rememberNewSentence(String userText) throws IOException {
        float currentHormoneValue;
        if (userText.contains("#")) currentHormoneValue = -1.0f;
        else currentHormoneValue = ModelApi.getEmotion(userText);
        hormones.add(currentHormoneValue);
        if (hormones.size() >= 10) hormones.poll();
        currentEmotion = calculateCurrentHormone();
        return Hormone.getHormone(currentEmotion);
    }

    /**
     * 호르몬 계산 메소드
     * 가중치를 부여해 현재 호르몬을 계산함
     * ∑(i = 0 to sizeOfQueue) Hi * Wi
     *
     * @return 계산된 현재 호르몬
     */
    private static float calculateCurrentHormone() {
        float currentHormones = 0.0f;
        int denominator = 0;
        for (int i = 1; i <= hormones.size(); i++) denominator += i;
        for (int i = 1; i <= hormones.size(); i++) currentHormones += (hormones.get(i - 1) * i);
        currentHormones /= denominator;
        return currentHormones;
    }

    /**
     * 호르몬 상태를 리턴해주는 클래스
     * 클라이언트는 이 메소드 사용하면 됩니다.
     *
     * @return 현재 호르몬 상태
     */
    public static Hormone getHormone() {
        return Hormone.getHormone(currentEmotion);
    }
}