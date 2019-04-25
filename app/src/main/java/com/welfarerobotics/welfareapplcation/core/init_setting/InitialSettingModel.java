package com.welfarerobotics.welfareapplcation.core.init_setting;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.welfarerobotics.welfareapplcation.core.base.BaseModel;
import com.welfarerobotics.welfareapplcation.entity.User;
import com.welfarerobotics.welfareapplcation.entity.UserCache;
import com.welfarerobotics.welfareapplcation.util.LiveData;
import com.welfarerobotics.welfareapplcation.util.UUID;
import java8.util.function.Consumer;
import lombok.Builder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @Author : Hyunwoong
 * @When : 4/24/2019 6:00 PM
 * @Homepage : https://github.com/gusdnd852
 */
public @Builder class InitialSettingModel extends BaseModel {

    private LiveData<ArrayList<ImageView>> photoImageViews;
    private LiveData showToastIfSuccessUpload;
    private LiveData showToastIfFailUpload;

    public void uploadUserDataForCache(User user) {
        UserCache.setInstance(user);
    }

    public void uploadUserDataForDatabase(User user) {
        String id = user.getId();
        FirebaseDatabase.getInstance()
                .getReference("user")
                .child(id)
                .setValue(user);// 디비 업로드
    }

    public void uploadPhoto(int rqCode, String uuid, Bitmap photo, Consumer<String> consumer) {
        photoImageViews.getValue().get(rqCode).setDrawingCacheEnabled(true);
        photoImageViews.getValue().get(rqCode).buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Objects.requireNonNull(photo).compress(Bitmap.CompressFormat.JPEG, 100, baos);

        FirebaseStorage.getInstance()
                .getReference()
                .child(uuid)
                .child("userPhoto" + rqCode)
                .putBytes(baos.toByteArray())
                .addOnFailureListener(exception -> showToastIfFailUpload.call())
                .addOnSuccessListener(taskSnapshot -> {
                    showToastIfSuccessUpload.call();
                    consumer.accept(taskSnapshot.getDownloadUrl().toString());
                });
    }
}
