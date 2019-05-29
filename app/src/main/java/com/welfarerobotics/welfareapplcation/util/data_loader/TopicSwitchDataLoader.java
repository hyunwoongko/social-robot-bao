package com.welfarerobotics.welfareapplcation.util.data_loader;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.entity.cache.ChatCache;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;

import java.util.ArrayList;

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
        Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
        ArrayList<String> qList = new ArrayList<>();
        for (DataSnapshot data : iterable) {
            String dataString = data.getValue(String.class);
            qList.add(dataString);
        }
        ChatCache.getInstance().setTopicSwitch(qList);
        System.out.println(getClass().getName() + " : 데이터 다운로드");
    }

    @Override public void load() {
        FirebaseHelper.get().download(FirebaseDatabase.getInstance()
                .getReference("chat")
                .child("topic switch"), this::save);
    }
}