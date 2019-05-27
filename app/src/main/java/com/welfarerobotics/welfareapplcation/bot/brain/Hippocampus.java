package com.welfarerobotics.welfareapplcation.bot.brain;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.intent.ChatIntent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/27/2019 4:12 PM
 * @homepage : https://github.com/gusdnd852
 * <p>
 * 해마를 모방한 클래스
 * 이전에 들은 말들을 기억함
 */
public @Getter class Hippocampus {

    private ArrayList<ChatIntent> openDomainIntentQueue = new ArrayList<>(); // 비목적 의도 큐
    private ChatIntent previousCloseDomainIntent = ChatIntent.builder().build(); // 최근 목적대화
    private String thoughtSentence = ""; // 지금 말하려고 생각한 문장

    private List<String> location = new ArrayList<>();
    private List<String> date = new ArrayList<>();
    private List<String> lang = new ArrayList<>();
    private List<String> word = new ArrayList<>();

    public void rememberWeather(List<String>[] entities) {
        this.location = entities[0];
        this.date = entities[1];
    }

    public void decideToSay(String thoughtSentence) {
        this.thoughtSentence = thoughtSentence;
    }

    public void clearAllEntity() {
        location.clear();
        date.clear();
        lang.clear();
        word.clear();
    }
}

