package com.welfarerobotics.welfareapplcation.bot.brain;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 1:59 AM
 * @homepage : https://github.com/gusdnd852
 * <p>
 * 기억을 망각하기 위한 클래스
 * Oblivion::forgetSomething 형태의 Runnable 단위로 전송
 */
public class Oblivion {
    public static void forgetLocation() {
        Brain.hippocampus.getLocation().clear();
    }

    public static void forgetDate() {
        Brain.hippocampus.getDate().clear();
    }

    public static void forgetLang() {
        Brain.hippocampus.getLang().clear();
    }

    public static void forgetWord() {
        Brain.hippocampus.getWord().clear();
    }

    public static void forgetAll() {
        Brain.hippocampus.getLocation().clear();
        Brain.hippocampus.getDate().clear();
        Brain.hippocampus.getLang().clear();
        Brain.hippocampus.getWord().clear();
    }
}
