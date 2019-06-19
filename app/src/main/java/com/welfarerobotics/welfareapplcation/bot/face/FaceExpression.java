package com.welfarerobotics.welfareapplcation.bot.face;

/**
 * @author : Hyunwoong
 * @when : 6/20/2019 5:11 AM
 * @homepage : https://github.com/gusdnd852
 */
public enum FaceExpression {

    happiness, neutral, anger, disgust, fear, contempt, sadness, surprise;

    @Override public String toString() {
        switch (this){
            case fear: return "fear";
            case anger: return "anger";
            case disgust: return "disgust";
            case sadness: return "sadness";
            case contempt: return "contempt";
            case surprise: return "surprise";
            case neutral: return "neutral";
            case happiness: return "happiness";
            default: return "neutral";
        }
    }
}
