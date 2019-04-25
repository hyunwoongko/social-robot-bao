package com.welfarerobotics.welfareapplcation.core.init_setting;

import android.Manifest;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;
import com.kinda.alert.KAlertDialog;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.main.MainActivity;
import com.welfarerobotics.welfareapplcation.databinding.InitialSettingView;
import com.welfarerobotics.welfareapplcation.util.ToastType;
import com.welfarerobotics.welfareapplcation.util.UUID;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class InitialSettingActivity extends BaseActivity<InitialSettingView, InitialSettingViewModel> {

    @Override public void onCreateView(int layout, Class<? extends AndroidViewModel> clazz) {
        super.onCreateView(R.layout.initial_setting_view, InitialSettingViewModel.class);
    }

    @Override public void observeData() {
        ArrayList<ImageView> photoImageViews = new ArrayList<>();
        photoImageViews.add(view.userImage0);
        photoImageViews.add(view.userImage1);
        photoImageViews.add(view.userImage2);
        viewModel.getPhotoStrings().setValue(new ArrayList<>());
        viewModel.getPhotoImageViews().setValue(photoImageViews);
        viewModel.getUuid().setValue(UUID.getId(this));
    }

    @Override public void observeAction() {
        viewModel.getShowToastIfUnder3PicturesEvent().observe(this, o -> showToast("3장 모두 업로드 해주세요.", ToastType.error));
        viewModel.getShowToastIfSuccessUpload().observe(this, o -> showToast("업로드에 성공하였습니다.", ToastType.success));
        viewModel.getShowToastIfFailUpload().observe(this, o -> showToast("업로드에 실패하였습니다.", ToastType.error));
        viewModel.getTakePictureToImageView0().observe(this, o -> takePicture(0));
        viewModel.getTakePictureToImageView1().observe(this, o -> takePicture(1));
        viewModel.getTakePictureToImageView2().observe(this, o -> takePicture(2));
        viewModel.getMoveToMainActivityEvent().observe(this, o -> {
            startActivity(new Intent(InitialSettingActivity.this, MainActivity.class));
            finish();
        });
    }

    private void takePicture(int rqCode) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, rqCode);
        else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, rqCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        if (grantResult[0] == 0) {
            showToast("권한이 승인되었습니다", ToastType.success);
            doTakePhotoAction(requestCode);
        } else showToast("카메라 권한이 승인되어야 이용 가능합니다.", ToastType.error);
    }

    private void doTakePhotoAction(int rqCode) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = createImageFile();
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this, "패키지명", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, rqCode);
                }
            } else showToast("외장 메모리 미지원", ToastType.error);
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(new Date());
        String imageFileName = timeStamp + ".jpg";
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/pathvalue/" + imageFileName);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.getProgressHelper().spin();
        pDialog.getProgressHelper().setSpinSpeed(1.0f);
        pDialog.setTitleText("업로드");
        pDialog.setContentText("\n이미지를 업로드 중입니다.\n\n");
        pDialog.setCancelable(false);
        pDialog.show();

        final Bundle extras = data.getExtras();
        assert extras == null;
        Bitmap photo = extras.getParcelable("data");
        viewModel.getPhotoImageViews()
                .getValue()
                .get(requestCode)
                .setImageBitmap(photo);
        viewModel.uploadToServer(requestCode, photo, uri -> {
            viewModel.getPhotoStrings().getValue().add(uri);
            pDialog.dismissWithAnimation();
        });
    }
}
