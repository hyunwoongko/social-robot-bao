package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.entity.cache.ChatCache;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 2:23 AM
 * @homepage : https://github.com/gusdnd852
 */
public class JokeScenario {
    private static Random random = new Random();
    private static List<String> jokes = Arrays.asList(
            "제가 재밌는 이야기 해드릴 게요.",
            "제가 웃긴 이야기 하나 해볼게요.",
            "제가 아는 재밌는 얘기 해드릴게요.",
            "아, 이 이야기 조금 재밌는데.. 하나 해드릴게요."
    );

    public static void process(String speech, Activity activity) throws IOException {
        String joke = jokes.get(random.nextInt(jokes.size() - 1));
        List<String> msgs = ChatCache.getInstance().getJoke();
        Random random = Brain.random;
        Brain.hippocampus.decideToSay(joke + " , " + msgs.get(random.nextInt(msgs.size() - 1)));
        Mouth.get().say(activity);
    }
}
