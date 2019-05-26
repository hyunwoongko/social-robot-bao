package com.welfarerobotics.welfareapplcation.bot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Hyunwoong
 * @when : 5/24/2019 5:10 PM
 * @homepage : https://github.com/gusdnd852
 * <p>
 * 현재 호르몬 상태를 가지고 있음
 */
@AllArgsConstructor
@NoArgsConstructor
public @Data class Emotion {
    private double adrenalin = 0; // 흥분, 불안
    private double cortisol = 0; // 스트레스, 긴장감
    private double dopamine = 0; // 사랑, 쾌락
    private double endorphin = 0; // 기쁨, 기분좋음
    private double noradrenalin = 0; // 분노, 화남
    private double serotonin = 0.2; // 편안함, 평온함
}

