package com.welfarerobotics.welfareapplcation.util.data_loader;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.entity.cache.ChatCache;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;

import java.util.ArrayList;

/**
 * @author : Hyunwoong
 * @when : 5/29/2019 1:44 PM
 * @homepage : https://github.com/gusdnd852
 */
public class CallDataLoader implements DataLoader {

    private static CallDataLoader instance;

    private CallDataLoader() {

    }

    static CallDataLoader getInstance() {
        if (instance == null) {
            instance = new CallDataLoader();
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
        ChatCache.getInstance().setCall(qList);
        System.out.println(getClass().getName() + " : 데이터 다운로드");
    }

    @Override public void load() {
        FirebaseHelper.get().download(FirebaseDatabase.getInstance()
                .getReference("chat")
                .child("call"), this::save);
    }
}
