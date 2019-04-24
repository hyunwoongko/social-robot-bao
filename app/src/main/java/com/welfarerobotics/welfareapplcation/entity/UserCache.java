package com.welfarerobotics.welfareapplcation.entity;

import lombok.Data;

import java.util.ArrayList;

public @Data class UserCache {
    private static UserCache instance = null;

    private UserCache() {
    }

    public synchronized static UserCache getInstance() {
        if (instance == null) {
            instance = new UserCache();
        }
        return instance;
    }

    public static void setInstance(User user) {
        UserCache cache = UserCache.getInstance();
        cache.setId(user.getId());
        cache.setName(user.getName());
        cache.setLocation(user.getLocation());
        cache.setDict(user.getDict());
        cache.setPhoto(user.getPhoto());
    }

    private String id;
    private String name;
    private String location;
    private ArrayList<Conversation> dict;
    private ArrayList<String> photo;
}
