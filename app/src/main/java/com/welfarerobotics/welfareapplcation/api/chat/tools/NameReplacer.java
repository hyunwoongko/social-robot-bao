package com.welfarerobotics.welfareapplcation.api.chat.tools;

import com.welfarerobotics.welfareapplcation.entity.UserCache;

/**
 * @Author : Hyunwoong
 * @When : 4/3/2019 10:15 AM
 * @Homepage : https://github.com/gusdnd852
 */
public final class NameReplacer {
    public static String replaceName(String input) {
        if (input.contains("당신")) {
            return input.replace("당신", UserCache.getInstance().getName() + " 님");
        }
        return input;
    }
}
