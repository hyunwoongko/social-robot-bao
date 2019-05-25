package com.welfarerobotics.welfareapplcation.api.chat.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 3:30 PM
 * @homepage : https://github.com/gusdnd852
 */
@NoArgsConstructor
@AllArgsConstructor
public @Data class HormoneModel {
    private List<String> answer;
    private int figures;
}
