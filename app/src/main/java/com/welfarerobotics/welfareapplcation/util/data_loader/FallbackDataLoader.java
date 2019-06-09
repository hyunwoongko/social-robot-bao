package com.welfarerobotics.welfareapplcation.util.data_loader;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.entity.cache.ChatCache;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;

import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 4:02 PM
 * @homepage : https://github.com/gusdnd852
 */
public class FallbackDataLoader implements DataLoader {

    private static FallbackDataLoader instance;

    private FallbackDataLoader() {

    }

    static FallbackDataLoader getInstance() {
        if (instance == null) {
            instance = new FallbackDataLoader();
        }
        return instance;
    }


    public void save(DataSnapshot dataSnapshot) {
        List<String> list = (List) dataSnapshot.getValue();
        ChatCache.getInstance().setFallback(list);
    }

    @Override public void load() {
        FirebaseHelper.get().connect(
                FirebaseDatabase.getInstance()
                .getReference("chat")
                .child("fallback"), this::save);
    }
}