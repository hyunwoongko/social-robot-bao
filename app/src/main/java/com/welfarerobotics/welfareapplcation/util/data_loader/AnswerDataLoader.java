package com.welfarerobotics.welfareapplcation.util.data_loader;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.welfarerobotics.welfareapplcation.api.chat.tools.AnswerModel;
import com.welfarerobotics.welfareapplcation.api.chat.tools.ChatCache;

import java.util.Map;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 7:25 PM
 * @homepage : https://github.com/gusdnd852
 */
public class AnswerDataLoader implements DataLoader {

    private static AnswerDataLoader instance;

    private AnswerDataLoader() {

    }

    static AnswerDataLoader getInstance() {
        if (instance == null) {
            instance = new AnswerDataLoader();
        }
        return instance;
    }

    @Override public void load() {
        FirebaseDatabase.getInstance().getReference("chat")
                .addValueEventListener(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot dataSnapshot) {
                        save(dataSnapshot);
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    @Override public void save(DataSnapshot snapshot) {
        Map<String, Map<String, AnswerModel>> map = (Map) snapshot.getValue();
        Map<String, AnswerModel> answerMap = map.get("answer");
        ChatCache.getInstance().setAnswer(answerMap);
    }
}
