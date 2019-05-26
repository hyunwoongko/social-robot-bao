package com.welfarerobotics.welfareapplcation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 3:29 PM
 * @homepage : https://github.com/gusdnd852
 */
@AllArgsConstructor
@NoArgsConstructor
public @Data class Answer {
    private Hormone adrenalin;
    private Hormone cortisol;
    private Hormone dopamine;
    private Hormone endorphin;
    private Hormone noradrenalin;
    private Hormone serotonin;
}
