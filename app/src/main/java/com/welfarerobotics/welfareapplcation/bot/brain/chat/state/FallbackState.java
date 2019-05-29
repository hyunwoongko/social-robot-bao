package com.welfarerobotics.welfareapplcation.bot.brain.chat.state;


import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.entity.cache.ChatCache;

import java.util.List;
import java.util.Random;

/**
 * @author : Hyunwoong
 * @when : 5/27/2019 5:33 PM
 * @homepage : https://github.com/gusdnd852
 */
public class FallbackState implements ChatState {
    private static FallbackState state;
    private Random random = new Random();

    private FallbackState() {
    }

    synchronized static FallbackState getInstance() {
        if (state == null) state = new FallbackState();
        return state;
    }

    @Override public ChatState think(String intent, String preprocessedSpeech) {
        ChatCache cache = ChatCache.getInstance();
        List<String> fallbackText = cache.getFallback();
        List<String> topicSwitch = cache.getTopicSwitch();

        String fallbackMsg = fallbackText.get(random.nextInt(fallbackText.size() - 1));
        String topicSwitchMsg = topicSwitch.get(random.nextInt(topicSwitch.size() - 1));
        Brain.hippocampus.decideToSay(fallbackMsg + " , " + topicSwitchMsg);
        return FALLBACK_STATE;
    }

    @Override public ChatState speech(Mouth mouth) {
        mouth.play(Brain.hippocampus.getThoughtSentence());
        return NORMAL_STATE;
    }
}
