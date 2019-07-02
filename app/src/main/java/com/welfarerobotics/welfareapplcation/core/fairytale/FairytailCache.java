package com.welfarerobotics.welfareapplcation.core.fairytale;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.welfarerobotics.welfareapplcation.entity.FairyTail;

import java.util.ArrayList;
import java.util.Iterator;

public class FairytailCache {
    private static FairytailCache instance;
    private static ArrayList<FairyTail> fairytail = new ArrayList<>();
    private String TAG ="FairytailCache";

    private FairytailCache() {
    }

    public synchronized static FairytailCache getInstance() {
        if (instance == null) {
            instance = new FairytailCache();
        }
        return instance;
    }

    public ArrayList<FairyTail> getFairytail(){

        return  fairytail;
    }
    public void clear(){
        fairytail.clear();
    }
    public void setFairytails(DataSnapshot fairytails) {
        FairyTail fairyTail;
        fairyTail = fairytails.getValue(FairyTail.class);
        fairytail.add( fairyTail);
        Log.d(TAG,fairyTail.getTitle());


    }

}
