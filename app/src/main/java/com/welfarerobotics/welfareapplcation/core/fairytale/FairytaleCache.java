package com.welfarerobotics.welfareapplcation.core.fairytale;

import com.welfarerobotics.welfareapplcation.api.chat.chatutil.Fairytale;

import java.util.ArrayList;

public class FairytaleCache {
    private static ArrayList<String> Fairytales = new ArrayList<>();
    private static FairytaleCache instance;

    private FairytaleCache() {
    }

    public synchronized static FairytaleCache getInstance() {
        if (instance == null) {
            instance = new FairytaleCache();
        }
        return instance;
    }   

    public void addFairytale(String item) {
        Fairytales.add(item);
    }

    public ArrayList<String> getFairytale(){
        return Fairytales;
    }
    
    public String getFairytale(int pos){
        return Fairytales.get(pos);
    }

    public int getFairytaleSize(){
        return Fairytales.size();
    }

    public void clear(){
        Fairytales = new ArrayList<>();
    }
}
