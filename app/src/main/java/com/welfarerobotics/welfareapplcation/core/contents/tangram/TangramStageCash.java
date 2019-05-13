package com.welfarerobotics.welfareapplcation.core.contents.tangram;

import java.util.ArrayList;

public class TangramStageCash {
    private static ArrayList<TangramListItem> backImgaes = new ArrayList<>();
    private static TangramStageCash instance;

    private TangramStageCash() {
    }

    public synchronized static TangramStageCash getInstance() {
        if (instance == null) {
            instance = new TangramStageCash();
        }
        return instance;
    }

    public void addImage(TangramListItem item) {
        backImgaes.add(item);

    }
    public ArrayList<TangramListItem> getImages(){
        return backImgaes;
    }

    public void clear(){
        backImgaes = new ArrayList<>();
    }
}
