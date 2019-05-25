package com.welfarerobotics.welfareapplcation.api.chat.tools;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author : Hyunwoong
 * @when : 5/24/2019 5:09 PM
 * @homepage : https://github.com/gusdnd852
 */

public @Data class ChatCache {
    private static ChatCache chatCache;

    private ChatCache() {
    }

    public static ChatCache getInstance() {
        if (chatCache == null) chatCache = new ChatCache();
        return chatCache;
    }

    private Map<String, AnswerModel> answer;
    private Map<String, List<String>> emotion;
    private List<String> question;
}
