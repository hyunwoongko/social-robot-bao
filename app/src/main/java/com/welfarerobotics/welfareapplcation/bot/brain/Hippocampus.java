package com.welfarerobotics.welfareapplcation.bot.brain;

import com.welfarerobotics.welfareapplcation.bot.brain.chat.preprocess.NameReplacer;
import com.welfarerobotics.welfareapplcation.entity.Alarm;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/27/2019 4:12 PM
 * @homepage : https://github.com/gusdnd852
 * <p>
 * 해마를 모방한 클래스
 * 이전에 들은 말들을 기억하는 클래스
 */
public @Getter class Hippocampus {

    private String previousIntent = ""; // 최근 목적대화
    private String thoughtSentence = ""; // 지금 말하려고 생각한 문장

    private List<String> location = new ArrayList<>();
    private List<String> date = new ArrayList<>();
    private List<String> lang = new ArrayList<>();
    private List<String> word = new ArrayList<>();

    public void rememberIntent(String intent) {
        this.previousIntent = intent;
    }

    public void rememberWeather(List<String>[] entities) {
        this.location.addAll(entities[0]);
        this.date.addAll(entities[1]);
    }

    public void rememberTranslate(List<String>[] entities) {
        word.addAll(entities[0]);
        lang.addAll(entities[1]);
    }

    public void rememberWord(List<String> entity) {
        word.addAll(entity);
    }

    public void rememberLocation(List<String> entity) {
        location.addAll(entity);
    }

    public void decideToSay(String thoughtSentence) {
        this.thoughtSentence = thoughtSentence;
    }
}

