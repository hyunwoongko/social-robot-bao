package com.welfarerobotics.welfareapplcation.util.data_loader;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.entity.cache.ChatCache;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 4:02 PM
 * @homepage : https://github.com/gusdnd852
 */
public class TopicSwitchDataLoader implements DataLoader {

    private static TopicSwitchDataLoader instance;

    private TopicSwitchDataLoader() {

    }

    static TopicSwitchDataLoader getInstance() {
        if (instance == null) {
            instance = new TopicSwitchDataLoader();
        }
        return instance;
    }


    public void save(DataSnapshot dataSnapshot) {
        List<String> list = (List) dataSnapshot.getValue();
        ChatCache.getInstance().setTopicSwitch(list);
    }

    @Override public void load() {
        FirebaseHelper.get().connect(FirebaseDatabase.getInstance()
                .getReference("chat")
                .child("topic switch"), this::save);
    }
}