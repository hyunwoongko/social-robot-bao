package com.welfarerobotics.welfareapplcation.core.fairytale;

import java.util.ArrayList;

public class FairytaleCache {
    private static ArrayList<String[]> fairytales = new ArrayList<>();
    private static FairytaleCache instance;

    private FairytaleCache() {
    }

    public synchronized static FairytaleCache getInstance() {
        if (instance == null) {
            instance = new FairytaleCache();
        }
        return instance;
    }

    public static void setFairytales(ArrayList<String[]> fairytales) {
        FairytaleCache.fairytales = fairytales;
    }

    public void addFairytale(String[]item) {
        fairytales.add(item);
    }

    public ArrayList<String[]> getFairytale() {
        return fairytales;
    }
    public String[]getFairytale(int pos) {
        return fairytales.get(pos);
    }
    public int getFairytaleSize() {
        return fairytales.size();
    }
    public void clear() {
        fairytales = new ArrayList<>();
    }
}
