package com.welfarerobotics.welfareapplcation.entity.cache;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.welfarerobotics.welfareapplcation.core.contents.tangram.TangramListItem;
import com.welfarerobotics.welfareapplcation.core.contents.tangram.TangramSeparater;
import com.welfarerobotics.welfareapplcation.entity.Tangram;

import java.util.ArrayList;

public class TangramStageCache {
    private static final String TAG = "탱그램 캐시" ;
    //    private static ArrayList<TangramListItem> backImgaes = new ArrayList<>();
    private static  ArrayList<String >urls = new ArrayList<String>();
    private static TangramStageCache instance;
    private static ArrayList<Tangram> tangrams = new ArrayList<Tangram>();

    private TangramStageCache() {
    }



    public synchronized static TangramStageCache getInstance() {
        if (instance == null) {
            instance = new TangramStageCache();
        }
        return instance;
    }
    public void addTangram(Tangram tangram){
        tangrams.add(tangram);

    }

    public ArrayList<Tangram> getTangrams(){

        return tangrams;
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

    public synchronized void setTangram(DataSnapshot snapshot) {
        Tangram tangram;
        tangram = snapshot.getValue(Tangram.class);
        tangrams.add(tangram);
        urls.add(tangram.getURL());
     //   TangramSeparater.Separate( tangram);
        Log.d(TAG,tangram.getPieces());

    }


}
