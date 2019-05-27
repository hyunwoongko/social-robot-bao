package com.welfarerobotics.welfareapplcation.bot.brain.chat.state;

import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.intent.ChatIntent;
import com.welfarerobotics.welfareapplcation.entity.cache.ChatCache;

import java.io.IOException;
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

    @Override public ChatState think(ChatIntent intent, String speech) throws IOException {
        ChatCache cache = ChatCache.getInstance(); // 캐시로드
        List<String> fallbackText = cache.getFallback(); // 폴백 대화
        List<String> topicSwitch = cache.getTopicSwitch(); // 화제 전환

        String fallbackMsg = fallbackText.get(random.nextInt(fallbackText.size() - 1));
        String topicSwitchMsg = topicSwitch.get(random.nextInt(topicSwitch.size() - 1));
        Brain.hippocampus.decideToSay(fallbackMsg + " , " + topicSwitchMsg);
        return this;
    }

    @Override public ChatState speech(Mouth mouth) {
        mouth.play(Brain.hippocampus.getThoughtSentence());
        return NORMAL_STATE;
    }
}