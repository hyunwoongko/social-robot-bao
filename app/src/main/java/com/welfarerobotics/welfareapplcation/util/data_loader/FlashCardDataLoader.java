package com.welfarerobotics.welfareapplcation.util.data_loader;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.entity.cache.FlashcardCache;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 7:27 PM
 * @homepage : https://github.com/gusdnd852
 */
public class FlashCardDataLoader implements DataLoader {

    private static FlashCardDataLoader instance;

    private FlashCardDataLoader() {

    }

    static FlashCardDataLoader getInstance() {
        if (instance == null) {
            instance = new FlashCardDataLoader();
        }
        return instance;
    }

    @Override public void load() {
        FirebaseHelper.get().download(FirebaseDatabase
                .getInstance()
                .getReference("flashcard"), this::save);
    }

    @Override public void save(DataSnapshot snapshot) {
        FlashcardCache.getInstance().addFlashcard(snapshot.getValue().toString().split(","));
        System.out.println(getClass().getName() + " : 데이터 다운로드");
    }
}
