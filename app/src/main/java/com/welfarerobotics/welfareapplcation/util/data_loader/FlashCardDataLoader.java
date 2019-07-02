package com.welfarerobotics.welfareapplcation.util.data_loader;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.core.fairytale.FairytailCache;
import com.welfarerobotics.welfareapplcation.entity.cache.FlashcardCache;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;

import java.util.Iterator;
import java.util.List;

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
        FlashcardCache.getInstance().clear();

        FirebaseHelper.get().download(FirebaseDatabase
                .getInstance()
                .getReference("word"), this::save);

    }

    @Override public void save(DataSnapshot snapshot) {
//        FlashcardCache.getInstance().addFlashcard(snapshot.getValue().toString().split(","));
        FlashcardCache.getInstance().setFlashcard(snapshot);



         }



    }

