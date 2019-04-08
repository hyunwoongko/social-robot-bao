package com.welfarerobotics.welfareapplcation.model;

import java.util.ArrayList;

public class UserSingleton {
    private static UserSingleton instance = null;

    private UserSingleton() {
    }

    public synchronized static UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }

    public static UserSingleton setInstance(UserModel model) {
        if (instance == null) {
            instance = new UserSingleton();
        }
        instance.setId(model.getId());
        instance.setName(model.getName());
        instance.setDict(model.getDict());
        instance.setLocation(model.getLocation());
        instance.setPhoto(model.getPhoto());
        return instance;
    }

    private String id;
    private String name;
    private String location;
    private ArrayList<ConversationModel> dict;
    private ArrayList<String> photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<ConversationModel> getDict() {
        return dict;
    }

    public void setDict(ArrayList<ConversationModel> dict) {
        this.dict = dict;
    }

    public ArrayList<String> getPhoto() {
        return photo;
    }

    public void setPhoto(ArrayList<String> photo) {
        this.photo = photo;
    }
}
