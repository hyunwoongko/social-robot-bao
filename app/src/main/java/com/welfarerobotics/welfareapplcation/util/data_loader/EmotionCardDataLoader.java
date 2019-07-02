package com.welfarerobotics.welfareapplcation.util.data_loader;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.entity.cache.EmotioncardCache;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 7:30 PM
 * @homepage : https://github.com/gusdnd852
 *
 * 감정카드게임 컨텐츠에 쓰이는 데이터
 */
public class EmotionCardDataLoader implements DataLoader {
    private static EmotionCardDataLoader instance;

    private EmotionCardDataLoader() {

    }

    static EmotionCardDataLoader getInstance() {
        if (instance == null) {
            instance = new EmotionCardDataLoader();
        }
        return instance;
    }

    @Override public void load() {
        EmotioncardCache.getInstance().clear();
        FirebaseHelper.get().download(FirebaseDatabase
                .getInstance()
                .getReference("emotioncard"), this::save);
    }

    @Override public void save(DataSnapshot snapshot) {
        EmotioncardCache.getInstance().addEmotioncard(snapshot.getValue().toString().split(","));
        System.out.println(getClass().getName() + " : 데이터 다운로드");
    }
}
