package com.welfarerobotics.welfareapplcation.api.chat.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 3:23 PM
 * @homepage : https://github.com/gusdnd852
 */
@NoArgsConstructor
@AllArgsConstructor
public @Data class ChatModel {
    private Map<String, AnswerModel> answer;
    private Map<String, List<String>> emotion;
    private List<String> question;
}
