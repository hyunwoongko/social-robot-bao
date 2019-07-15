package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.conversation;

import android.content.Context;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.entity.cache.ChatCache;

import java.util.List;
import java.util.Random;

/**
 * @author : Hyunwoong
 * @when : 5/31/2019 12:57 PM
 * @homepage : https://github.com/gusdnd852
 */
public class FallbackScenario {
    public static void process(Context context) {
        List<String> fallbacks = ChatCache.getInstance().getFallback();
        Random random = Brain.random;

        String fallback = fallbacks.get(random.nextInt(fallbacks.size() - 1));
        Brain.hippocampus.decideToSay(fallback);
        Mouth.get().say(context);
    }
}
