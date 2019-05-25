package com.welfarerobotics.welfareapplcation.api.chat.tools;

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
public @Data class AnswerModel {
    private HormoneModel adrenalin;
    private HormoneModel cortisol;
    private HormoneModel dopamine;
    private HormoneModel endorphin;
    private HormoneModel noradrenalin;
    private HormoneModel serotonin;
}
