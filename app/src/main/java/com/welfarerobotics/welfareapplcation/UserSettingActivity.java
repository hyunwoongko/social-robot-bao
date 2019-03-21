package com.welfarerobotics.welfareapplcation;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UserSettingActivity extends AppCompatActivity {
    private final int REQUEST_TAKE_PICTURE = 1;
    private ImageView iv_UserPhoto;
    private String mCurrentPhotoPath;
    private Uri photoURI;
    private FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting);
        storage = FirebaseStorage.getInstance();

        iv_UserPhoto = this.findViewById(R.id.imageView);
        iv_UserPhoto.setOnClickListener(view -> {
            int permissionCheck = ContextCompat.checkSelfPermission(UserSettingActivity.this, Manifest.permission.CAMERA);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                //권한없음
                ActivityCompat.requestPermissions(UserSettingActivity.this, new String[]{Manifest.permission.CAMERA
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                //ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
            } else {
                //권한있음
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        if (requestCode == 0) {
            if (grantResult[0] == 0) {
                Toast.makeText(this, "권한이 승인되었습니다", Toast.LENGTH_SHORT).show();
                doTakePhotoAction();
            } else {
                //권한이 거절된경우
                Toast.makeText(this, "카메라권한이 거절되었습니다." +
                        " 카메라를 이용하려면 권한을 승낙하여야합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //카메라에서 사진촬영
    private void doTakePhotoAction()// 카메라 찰영후 이미지가져오기
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
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
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

        if (resultCode != RESULT_OK)
            return;
        if (requestCode == REQUEST_TAKE_PICTURE) {
            final Bundle extras = data.getExtras();
            assert extras == null;
            //Bitmap photo = ImageUtil.cropCenter(extras.getParcelable("data")); centor crop기능 (extras.getParcelable("data"));
            Bitmap photo = extras.getParcelable("data");
            iv_UserPhoto.setImageBitmap(photo);
            Toast.makeText(getApplicationContext(), "촬영 성공", Toast.LENGTH_SHORT).show();

            StorageReference storageRef = storage.getReference("social-robot-bao.appspot.com");
            iv_UserPhoto.setDrawingCacheEnabled(true);
            iv_UserPhoto.buildDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Objects.requireNonNull(photo).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datas = baos.toByteArray();

            UploadTask uploadTask = storageRef.putBytes(datas);
            uploadTask.addOnFailureListener(exception -> {
                Toast.makeText(this, "업로드 실패", Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(getApplicationContext(), taskSnapshot.getDownloadUrl().toString(), Toast.LENGTH_SHORT).show();
                //이미지 URI을 RealTime Database에 upload
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message");
                myRef.setValue(taskSnapshot.getDownloadUrl().toString());
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            });

        }
    }
}
