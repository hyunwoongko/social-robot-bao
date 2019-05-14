package com.welfarerobotics.welfareapplcation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author : Hyunwoong
 * @When : 5/13/2019 8:46 PM
 * @Homepage : https://github.com/gusdnd852
 */
@AllArgsConstructor
@NoArgsConstructor
public @Data class Hormone {
    private double adrenalin = 0; // 흥분, 불안
    private double cortisol = 0; // 스트레스, 긴장감
    private double dopamine = 0; // 사랑, 쾌락
    private double endorphin = 0; // 기쁨, 기분좋음
    private double noradrenalin = 0; // 분노, 화남
    private double serotonin = 0.2; // 편안함, 평온함
}
