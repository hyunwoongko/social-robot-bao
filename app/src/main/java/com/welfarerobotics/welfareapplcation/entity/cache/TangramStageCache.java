package com.welfarerobotics.welfareapplcation.entity.cache;

import com.welfarerobotics.welfareapplcation.core.contents.tangram.TangramListItem;

import java.util.ArrayList;

public class TangramStageCache {
    private static ArrayList<TangramListItem> backImgaes = new ArrayList<>();
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

    public void addImage(TangramListItem item) {
        backImgaes.add(item);

    }
    public void addurl(String item) {
       urls.add(item);

    }
    public ArrayList<String> getUrls() {
        return  urls;

    }
    public ArrayList<TangramListItem> getImages(){
        return backImgaes;
    }

    public void clear(){
        backImgaes = new ArrayList<>();
    }
}
