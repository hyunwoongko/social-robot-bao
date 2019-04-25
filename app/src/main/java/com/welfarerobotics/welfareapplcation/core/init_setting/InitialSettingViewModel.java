package com.welfarerobotics.welfareapplcation.core.init_setting;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import com.welfarerobotics.welfareapplcation.core.base.BaseViewModel;
import com.welfarerobotics.welfareapplcation.entity.User;
import com.welfarerobotics.welfareapplcation.util.LiveData;
import com.welfarerobotics.welfareapplcation.util.MethodOnXml;
import com.welfarerobotics.welfareapplcation.util.Preference;
import java8.util.function.Consumer;
import lombok.Getter;

import java.util.ArrayList;

/**
 * @Author : Hyunwoong
 * @When : 4/24/2019 6:00 PM
 * @Homepage : https://github.com/gusdnd852
 */
public @Getter class InitialSettingViewModel extends BaseViewModel {

    private LiveData<String> uuid = new LiveData<>();
    private LiveData<ArrayList<String>> photoStrings = new LiveData<>();
    private LiveData<ArrayList<ImageView>> photoImageViews = new LiveData<>();
    private LiveData<String> nameData = new LiveData<>();
    private LiveData<String> locationData = new LiveData<>();

    private LiveData showToastIfUnder3PicturesEvent = new LiveData();
    private LiveData showToastIfSuccessUpload = new LiveData();
    private LiveData showToastIfFailUpload = new LiveData();
    private LiveData moveToMainActivityEvent = new LiveData();

    private LiveData takePictureToImageView0 = new LiveData();
    private LiveData takePictureToImageView1 = new LiveData();
    private LiveData takePictureToImageView2 = new LiveData();

    private InitialSettingModel model = InitialSettingModel.builder()
            .photoImageViews(photoImageViews)
            .showToastIfSuccessUpload(showToastIfSuccessUpload)
            .showToastIfFailUpload(showToastIfFailUpload)
            .build();

    public InitialSettingViewModel(@NonNull Application application) {
        super(application);
    }

    @MethodOnXml
    public void nextButtonClicked(View view) {
        if (photoImageViews.getValue().size() < 3) {
            showToastIfUnder3PicturesEvent.call();
        } else {
            User user = User.builder()
                    .id(uuid.getValue())
                    .name(nameData.getValue())
                    .location(locationData.getValue())
                    .photo(photoStrings.getValue())
                    .build();

            model.uploadUserDataForCache(user); // 싱글톤 업로드
            model.uploadUserDataForDatabase(user); // 디비 업로드
            Preference.get(getContext()).setBoolean("firstUser", false);
            moveToMainActivityEvent.call();
        }
    }

    @MethodOnXml
    public void userImage0Clicked(View view) {
        takePictureToImageView0.call();
    }

    @MethodOnXml
    public void userImage1Clicked(View view) {
        takePictureToImageView1.call();
    }

    @MethodOnXml
    public void userImage2Clicked(View view) {
        takePictureToImageView2.call();
    }

    public void uploadToServer(int rqCode, Bitmap photo, Consumer<String> consumer) {
        model.uploadPhoto(rqCode, uuid.getValue(), photo, consumer);
    }
}
