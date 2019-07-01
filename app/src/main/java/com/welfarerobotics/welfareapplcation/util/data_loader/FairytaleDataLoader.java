package com.welfarerobotics.welfareapplcation.util.data_loader;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.core.fairytale.FairytaleCache;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 7:22 PM
 * @homepage : https://github.com/gusdnd852
 */
public class FairytaleDataLoader implements DataLoader {


    private static FairytaleDataLoader instance;

    private FairytaleDataLoader() {

    }

    static FairytaleDataLoader getInstance() {
        if (instance == null) {
            instance = new FairytaleDataLoader();
        }
        return instance;
    }


    @Override public void load() {

    }

    @Override public void save(DataSnapshot snapshot) {

    }
}
