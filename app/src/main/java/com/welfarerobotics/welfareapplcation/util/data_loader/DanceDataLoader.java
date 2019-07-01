package com.welfarerobotics.welfareapplcation.util.data_loader;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.bot.Body.DanceCache;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;

import java.util.ArrayList;
import java.util.Iterator;

public class DanceDataLoader implements DataLoader {
    static private DanceDataLoader instance = null;

    static DanceDataLoader getInstance() {
        if (instance == null) {
            instance = new DanceDataLoader();
        }
        return instance;
    }

    @Override
    public void load() {

    }


    @Override
    public void save(DataSnapshot snapshot) {

    }
}
