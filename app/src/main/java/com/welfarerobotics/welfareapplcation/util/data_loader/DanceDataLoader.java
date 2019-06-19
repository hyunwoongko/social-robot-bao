package com.welfarerobotics.welfareapplcation.util.data_loader;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.bot.Body.DanceCashe;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;

import java.util.ArrayList;
import java.util.Iterator;

public class DanceDataLoader implements DataLoader {
    static private DanceDataLoader instance =null;

    static DanceDataLoader getInstance() {
        if (instance == null) {
            instance = new DanceDataLoader();
        }
        return instance;
    }

    @Override
    public void load() {
        FirebaseHelper.get().download(FirebaseDatabase
                .getInstance()
                .getReference("dance"), this::save);

    }


    @Override
    public void save(DataSnapshot snapshot) {
        Iterator<DataSnapshot> snap = snapshot.getChildren().iterator();
        System.out.println("데이터 실행");
        ArrayList<String> command = new ArrayList<>();
        for(;snap.hasNext();){
            command.add(snap.next().getValue().toString());
        }

        DanceCashe.getInstance().addCommans(command);
    }
}
