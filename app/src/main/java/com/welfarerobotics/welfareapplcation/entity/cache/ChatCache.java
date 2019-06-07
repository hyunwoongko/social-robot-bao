package com.welfarerobotics.welfareapplcation.entity.cache;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
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

    private List<String> call = new ArrayList<>();
    private Map<String, List<String>> emotion = new HashMap<>();
    private List<String> fallback = new ArrayList<>();
    private List<String> joke = new ArrayList<>();
}
