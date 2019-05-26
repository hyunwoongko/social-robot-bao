package com.welfarerobotics.welfareapplcation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class Quiz {
    private String question;
    private boolean answer;
}
