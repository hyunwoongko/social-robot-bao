package com.welfarerobotics.welfareapplcation.bot.chat.intent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/24/2019 4:50 PM
 * @homepage : https://github.com/gusdnd852
 * <p>
 * 인텐트별 데이터를 캡슐화하는 클래스
 */
@AllArgsConstructor
@NoArgsConstructor
public @Builder @Data class ChatIntent {
    private String intentName;

    private int adrenalin;
    private int cortisol;
    private int dopamine;
    private int endorphin;
    private int noradrenalin;
    private int serotonin;

    private List<String> adrenalinAnswer;
    private List<String> cortisolAnswer;
    private List<String> dopamineAnswer;
    private List<String> endorphinAnswer;
    private List<String> noradrenalinAnswer;
    private List<String> serotoninAnswer;
}
