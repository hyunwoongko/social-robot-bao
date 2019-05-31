package com.welfarerobotics.welfareapplcation.util.data_loader;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.entity.cache.ChatCache;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/29/2019 3:29 PM
 * @homepage : https://github.com/gusdnd852
 */
public class JokeDataLoader implements DataLoader {

    private static JokeDataLoader instance;

    private JokeDataLoader() {

    }

    static JokeDataLoader getInstance() {
        if (instance == null) {
            instance = new JokeDataLoader();
        }
        return instance;
    }


    public void save(DataSnapshot dataSnapshot) {
        List<String> list = (List) dataSnapshot.getValue();
        ChatCache.getInstance().setJoke(list);

    }

    @Override public void load() {
        FirebaseHelper.get().connect(FirebaseDatabase.getInstance()
                .getReference("chat")
                .child("joke"), this::save);
    }
}
