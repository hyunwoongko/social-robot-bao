package com.welfarerobotics.welfareapplcation.util.data_loader;

import android.graphics.Bitmap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.core.contents.tangram.TangramListItem;
import com.welfarerobotics.welfareapplcation.core.fairytale.FairytaleCache;
import com.welfarerobotics.welfareapplcation.entity.cache.TangramStageCache;
import com.welfarerobotics.welfareapplcation.util.Pool;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;
import com.welfarerobotics.welfareapplcation.util.data_util.UrlConverter;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 7:17 PM
 * @homepage : https://github.com/gusdnd852
 */
public class TangramDataLoader implements DataLoader {

    private static TangramDataLoader instance;

    private TangramDataLoader() {

    }

    static TangramDataLoader getInstance() {
        if (instance == null) {
            instance = new TangramDataLoader();
        }
        return instance;
    }

    @Override
    public void load() {
        FirebaseHelper.get().download(FirebaseDatabase
                .getInstance()
                .getReference("tangram")
                .child("background"), this::save);
    }


    @Override
    public void save(DataSnapshot snapshot) {
        Pool.imageThread.execute(() -> {
            TangramListItem myItem;
            Bitmap stage = UrlConverter.convertUrl(snapshot.getValue().toString());
            myItem = new TangramListItem();
            myItem.setStage(stage);
            TangramStageCache.getInstance().addImage(myItem);
            System.out.println(getClass().getName() + " : 데이터 다운로드");
        });
    }
}
