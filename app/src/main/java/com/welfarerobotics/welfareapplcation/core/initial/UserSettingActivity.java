package com.welfarerobotics.welfareapplcation.core.initial;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.main.MainActivity;
import com.welfarerobotics.welfareapplcation.entity.User;
import com.welfarerobotics.welfareapplcation.entity.UserCache;
import com.welfarerobotics.welfareapplcation.util.Preference;
import com.welfarerobotics.welfareapplcation.util.UUID;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UserSettingActivity extends AppCompatActivity {
    private ArrayList<ImageView> iv_UserPhotos;
    private String mCurrentPhotoPath;
    private Uri photoURI;
    private FirebaseStorage storage;
    private ArrayList<String> photos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.initial_user);

        storage = FirebaseStorage.getInstance();
        ImageView nextButton = findViewById(R.id.next_imageView_userScreen);

        iv_UserPhotos = new ArrayList<>();
        iv_UserPhotos.add((ImageView) findViewById(R.id.imageView3));
        iv_UserPhotos.add((ImageView) findViewById(R.id.imageView4));
        iv_UserPhotos.add((ImageView) findViewById(R.id.imageView5));

        iv_UserPhotos.get(0).setOnClickListener(view -> captureImage(0));
        iv_UserPhotos.get(1).setOnClickListener(view -> captureImage(1));
        iv_UserPhotos.get(2).setOnClickListener(view -> captureImage(2));
        nextButton.setOnClickListener(v -> {
            if (photos.size() < 3) {
                Toast.makeText(UserSettingActivity.this, "사용자의 사진 3장을 업로드 해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                String id = UUID.getId(this);
                User model = new User();
                model.setId(id);
                model.setName(((EditText) findViewById(R.id.editusernameView)).getText().toString());
                model.setLocation("전주"); // TODO : 지역 Spinner 구현해야함
                model.setPhoto(photos);
                UserCache.setInstance(model);
                // 싱글톤 업로드

                FirebaseDatabase.getInstance()
                        .getReference("user")
                        .child(id)
                        .setValue(model);
                // 디비 업로드

                Preference.get(UserSettingActivity.this).setBoolean("firstUser", false);
                startActivity(new Intent(UserSettingActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void captureImage(int rqCode) {
        int permissionCheck = ContextCompat.checkSelfPermission(UserSettingActivity.this, Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            //권한없음
            ActivityCompat.requestPermissions(UserSettingActivity.this, new String[]{Manifest.permission.CAMERA
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE}, rqCode);
            //ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        } else {
            //권한있음
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, rqCode);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        if (grantResult[0] == 0) {
            Toast.makeText(this, "권한이 승인되었습니다", Toast.LENGTH_SHORT).show();
            doTakePhotoAction(requestCode);
        } else {
            //권한이 거절된경우
            Toast.makeText(this, "카메라권한이 거절되었습니다." +
                    " 카메라를 이용하려면 권한을 승낙하여야합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    //카메라에서 사진촬영
    private void doTakePhotoAction(int rqCode)// 카메라 찰영후 이미지가져오기
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = createImageFile();
                if (photoFile != null) {
                    // getURLForFile의 두번째 인자는 Manifest provider의 authorites와 일치해야함
                    //photoURI : fill://로 시작, FileProvider(Content Provider 하위)sms content:// 로시작
                    //누가(7.0)이상부터는 file://로 시작되는 Uri의 값을 주고받기 불가능
                    photoURI = FileProvider.getUriForFile(this, "패키지명", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, rqCode);
                }
            } else {
                Toast.makeText(UserSettingActivity.this, "외장메모리 미 지원", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private File createImageFile() {
        //이미지 파일 이름 생성
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/pathvalue/" + imageFileName);

        //Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = storageDir.getAbsolutePath();
        Log.i("mCurrentPhotoPath", mCurrentPhotoPath);
        return storageDir;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Bundle extras = data.getExtras();
        assert extras == null;
        //Bitmap photo = ImageUtil.cropCenter(extras.getParcelable("data")); centor crop기능 (extras.getParcelable("data"));
        Bitmap photo = extras.getParcelable("data");
        iv_UserPhotos.get(requestCode).setImageBitmap(photo);
        Toast.makeText(getApplicationContext(), "촬영 성공", Toast.LENGTH_SHORT).show();

        StorageReference storageRef = storage.getReference()
                .child(UUID.getId(this))
                .child("userPhoto" + requestCode);

        iv_UserPhotos.get(requestCode).setDrawingCacheEnabled(true);
        iv_UserPhotos.get(requestCode).buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Objects.requireNonNull(photo).compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datas = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(datas);
        uploadTask.addOnFailureListener(exception -> {
            Toast.makeText(this, "사진 업로드에 실패였습니다", Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(getApplicationContext(), "사진을 업로드 하였습니다.", Toast.LENGTH_SHORT).show();
            //이미지 URI을 RealTime Database에 upload
            photos.add(taskSnapshot.getDownloadUrl().toString());
        });
    }
}
