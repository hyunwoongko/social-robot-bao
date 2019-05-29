package com.welfarerobotics.welfareapplcation.bot.ear;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.util.Sound;
import java8.util.function.Consumer;

import java.util.Arrays;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/29/2019 12:37 AM
 * @homepage : https://github.com/gusdnd852
 */
public class Attention {

    private List<String> attentionSet = Arrays.asList(
            "바오", "바오야", "다우", "다우야", "바우", "바우야", "바보야",
            "하이", "헬로", "hi", "hello", "안녕"
    );

    private boolean detectCalling(String speech) {
        System.out.println(speech);
        boolean recognization = false;
        speech = speech.trim();
        speech = speech.toLowerCase();
        for (String attention : attentionSet)
            if (speech.contains(attention)) recognization = true;
        return recognization;
    }

    public void focus(String s, Activity activity, Ear ear, Consumer<String> consumer) {
        if (detectCalling(s)) {
            Sound.get().effectSound(activity, R.raw.speech_on);
            consumer.accept(s);
        } else {
            ear.hearAgain();
        }
    }

    public void block(Activity activity) {
        Sound.get().effectSound(activity, R.raw.speech_off);
    }
}
