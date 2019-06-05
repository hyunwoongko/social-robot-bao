package com.welfarerobotics.welfareapplcation.core.contents.paintwith;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.initial.InitialWifiActivity;
import com.welfarerobotics.welfareapplcation.core.settings.WifiActivity;
import com.welfarerobotics.welfareapplcation.util.CanvasView;
import com.welfarerobotics.welfareapplcation.util.DeviceId;
import com.welfarerobotics.welfareapplcation.util.ToastType;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

/**
 * @Author : Hyunwoong
 * @When : 4/9/2019 1:51 AM
 * @Homepage : https://github.com/gusdnd852
 */
public class PaintWithActivity extends BaseActivity {

    private CanvasView canvasView;
    private FirebaseStorage storage;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_with);
        canvasView = findViewById(R.id.canvas_view);
        canvasView.setBackgroundColor(Color.TRANSPARENT);

        findViewById(R.id.redo_button).setOnClickListener(v -> canvasView.redo());
        findViewById(R.id.undo_button).setOnClickListener(v -> canvasView.undo());
        findViewById(R.id.clear_button).setOnClickListener(v -> canvasView.clear());
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        findViewById(R.id.pencil_button).setOnClickListener(v -> {
            this.canvasView.setPaintStrokeWidth(5F);
            this.canvasView.setMode(CanvasView.Mode.DRAW);    // for drawing
        });
        findViewById(R.id.eraser_button).setOnClickListener(v -> {
            this.canvasView.setPaintStrokeWidth(40F);
            this.canvasView.setMode(CanvasView.Mode.ERASER);  // for using Eraser
        });

        findViewById(R.id.style_button).setOnClickListener(v -> {
            StorageReference storageRef = storage.getReference()
                    .child(DeviceId.getInstance(this).getUUID())
                    .child("paintWith");

            Bitmap bitmap = this.canvasView.getScaleBitmap(256, 256);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Objects.requireNonNull(bitmap).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datas = baos.toByteArray();
            UploadTask uploadTask = storageRef.putBytes(datas);
            uploadTask.addOnFailureListener(exception -> {

                showToast("사진 업로드에 실패하였습니다. 연결상태를 확인해주세요.", ToastType.error);
                startActivity(new Intent(this, WifiActivity.class));

            }).addOnSuccessListener(taskSnapshot -> {
                taskSnapshot.getDownloadUrl().toString();
            });
        });
    }
}
