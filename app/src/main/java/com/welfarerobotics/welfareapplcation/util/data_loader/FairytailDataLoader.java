package com.welfarerobotics.welfareapplcation.util.data_loader;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.core.fairytale.FairytailCache;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;

public class FairytailDataLoader implements DataLoader {

    private static FairytailDataLoader instance;

    static FairytailDataLoader getInstance() {
        if (instance == null) {
            instance = new FairytailDataLoader();
        }
        return instance;
    }


    @Override
    public void load() {
        FirebaseHelper.get().download(FirebaseDatabase
                .getInstance()
                .getReference("fairytail"), this::save);
    }

    @Override
    public void save(DataSnapshot snapshot) {
        Log.d("캐시 테스트",snapshot.toString());
        FairytailCache.getInstance().setFairytails(snapshot);
    }
}
