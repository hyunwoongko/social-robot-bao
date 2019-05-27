package com.welfarerobotics.welfareapplcation.bot.brain;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.intent.ChatIntent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Hyunwoong
 * @when : 5/24/2019 5:10 PM
 * @homepage : https://github.com/gusdnd852
 * <p>
 * 편도체를 모방한 클래스
 * 감정에 반응하고 현재  감정 상태를관할함
 */
@AllArgsConstructor
@NoArgsConstructor
public @Data class Amylace {
    private double adrenalin = 0; // 흥분, 불안
    private double cortisol = 0; // 스트레스, 긴장감
    private double dopamine = 0; // 사랑, 쾌락
    private double endorphin = 0; // 기쁨, 기분좋음
    private double noradrenalin = 0; // 분노, 화남
    private double serotonin = 0.5; // 편안함, 평온함

    public void feel(ChatIntent intent) {

    }

    public void express() {

    }
}

