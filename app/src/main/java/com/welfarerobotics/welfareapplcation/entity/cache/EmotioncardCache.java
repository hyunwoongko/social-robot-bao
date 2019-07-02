package com.welfarerobotics.welfareapplcation.entity.cache;

import java.util.ArrayList;

public class EmotioncardCache {
    private static ArrayList<String[]> emotioncards = new ArrayList<>();
    private static EmotioncardCache instance;

    private EmotioncardCache() {
    }

    public synchronized static EmotioncardCache getInstance() {
        if (instance == null) {
            instance = new EmotioncardCache();
        }
        return instance;
    }

    public void addEmotioncard(String[] item) {
        emotioncards.add(item);
    }

    public ArrayList<String[]> getEmotioncard() {
        return emotioncards;
    }

    public String[] getEmotioncard(int pos) {
        return emotioncards.get(pos);
    }

    public int getEmotioncardSize() {
        return emotioncards.size();
    }

    public void clear() {
        emotioncards.clear();
    }
}
