package com.welfarerobotics.welfareapplcation.entity.cache;

import com.welfarerobotics.welfareapplcation.core.contents.tangram.TangramListItem;

import java.util.ArrayList;

public class TangramStageCache {
//    private static ArrayList<TangramListItem> backImgaes = new ArrayList<>();
    private static  ArrayList<String >urls = new ArrayList<String>();
    private static TangramStageCache instance;

    private TangramStageCache() {
    }

    public synchronized static TangramStageCache getInstance() {
        if (instance == null) {
            instance = new TangramStageCache();
        }
        return instance;
    }

    public void addURL(String item) {
       urls.add(item);

    }
    public ArrayList<String> getURL() {
        return  urls;

    }

    public void clear(){
        urls.clear();
    }
}
