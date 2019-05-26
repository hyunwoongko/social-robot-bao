package com.welfarerobotics.welfareapplcation.util.data_loader;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.welfarerobotics.welfareapplcation.entity.cache.ChatCache;

import java.util.List;
import java.util.Map;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 8:42 PM
 * @homepage : https://github.com/gusdnd852
 * <p>
 * 대화시 감정 인식에 쓰이는 데이터
 */
public class EmotionChatDataLoader implements DataLoader {

    private static EmotionChatDataLoader instance;

    private EmotionChatDataLoader() {

    }

    static EmotionChatDataLoader getInstance() {
        if (instance == null) {
            instance = new EmotionChatDataLoader();
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
        Map<String, Map<String, List<String>>> map = (Map) snapshot.getValue();
        Map<String, List<String>> answerMap = map.get("emotion");
        ChatCache.getInstance().setEmotion(answerMap);
        System.out.println(ChatCache.getInstance().getEmotion());
    }
}
