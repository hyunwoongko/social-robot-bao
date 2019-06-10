package com.welfarerobotics.welfareapplcation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class DictationQuiz {
    private String imageURL;
    private String word;
}
