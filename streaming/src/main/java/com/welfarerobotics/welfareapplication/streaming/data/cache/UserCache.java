package com.welfarerobotics.welfareapplication.streaming.data.cache;

import com.welfarerobotics.welfareapplication.streaming.base.marker.Cache;
import com.welfarerobotics.welfareapplication.streaming.data.entity.User;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 2:34
 * @homepage : https://github.com/gusdnd852
 */
public class UserCache extends Cache<User> {
    private static UserCache instance;

    public static UserCache getInstance() {
        if (instance == null) instance = new UserCache();
        return instance;
    }

    private UserCache() {
    }
}
