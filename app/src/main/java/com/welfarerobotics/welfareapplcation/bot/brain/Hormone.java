package com.welfarerobotics.welfareapplcation.bot.brain;

/**
 * @author : Hyunwoong
 * @when : 6/16/2019 10:51 AM
 * @homepage : https://github.com/gusdnd852
 */
public enum Hormone {
    Dopamine, Endorphin, Serotonin, Cortisol, Noradrenalin;

    /**
     * 현재 감정지수를 입력하면 호르몬을 리턴함
     *
     * @param currEmotion 현재 감정지수
     * @return 호르몬 상태
     */
    public static Hormone getHormone(float currEmotion) {
        if (currEmotion >= 0.6) return Dopamine; // 쾌락
        else if (currEmotion < 0.6 && currEmotion >= 0.2) return Endorphin; // 기쁨
        else if (currEmotion < 0.2 && currEmotion >= -0.2) return Serotonin; // 평온
        else if (currEmotion < -0.2 && currEmotion >= -0.7) return Cortisol; // 스트레스
        else if (currEmotion < -0.7) return Noradrenalin; // 분노
        else return null;
    }

    public String toString() {
        switch (this){
            case Dopamine: return "Dopamine";
            case Endorphin: return "Endorphin";
            case Serotonin: return "Serotonin";
            case Cortisol: return "Cortisol";
            case Noradrenalin: return "Noradrenalin";
            default: return "NULL";
        }
    }
}
