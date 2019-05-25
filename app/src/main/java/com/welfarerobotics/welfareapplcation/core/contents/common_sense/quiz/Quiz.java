package com.welfarerobotics.welfareapplcation.core.contents.common_sense.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class Quiz {
    private String question;
    private boolean answer;
}
