package com.welfarerobotics.welfareapplcation.core.contents.paintwith;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kinda.alert.KAlertDialog;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.bot.brain.paint.PainterApi;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.settings.WifiActivity;
import com.welfarerobotics.welfareapplcation.util.CanvasView;
import com.welfarerobotics.welfareapplcation.util.DeviceId;
import com.welfarerobotics.welfareapplcation.util.Pool;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author : Hyunwoong
 * @When : 4/9/2019 1:51 AM
 * @Homepage : https://github.com/gusdnd852
 */
public class PaintWithActivity extends BaseActivity {

    private CanvasView canvasView;
    private String uuid;
    private KAlertDialog pDialog;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_with);
        canvasView = findViewById(R.id.canvas_view);
        canvasView.setBackgroundColor(Color.TRANSPARENT);

        uuid = DeviceId.getInstance(this).getUUID();
        findViewById(R.id.redo_button).setOnClickListener(v -> canvasView.redo());
        findViewById(R.id.undo_button).setOnClickListener(v -> canvasView.undo());
        findViewById(R.id.clear_button).setOnClickListener(v -> canvasView.clear());
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);

        ImageView imageView = findViewById(R.id.paint_with_image);

        findViewById(R.id.pencil_button).setOnClickListener(v -> {
            this.canvasView.setPaintStrokeWidth(5F);
            this.canvasView.setMode(CanvasView.Mode.DRAW);    // for drawing
        });
        findViewById(R.id.eraser_button).setOnClickListener(v -> {
            this.canvasView.setPaintStrokeWidth(40F);
            this.canvasView.setMode(CanvasView.Mode.ERASER);  // for using Eraser
        });

        findViewById(R.id.style_button).setOnClickListener(v -> {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("\n바오가 그림을 그리는 중이에요. \n");
            pDialog.setCancelable(false);
            pDialog.show();

            StorageReference storageRef = FirebaseStorage.getInstance()
                    .getReference()
                    .child(uuid + ".jpg");

            Bitmap bitmap = this.canvasView.getScaleBitmap(256, 256);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Objects.requireNonNull(bitmap).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datas = baos.toByteArray();
            UploadTask uploadTask = storageRef.putBytes(datas);
            uploadTask.addOnFailureListener(exception -> this.exception()).addOnSuccessListener(taskSnapshot -> {
                Future<String> urlFuture = Pool.threadPool.submit(() -> PainterApi.getPaint(uuid, taskSnapshot.getDownloadUrl().toString().split("\\?")[0]));
                new Thread(() -> runOnUiThread(() -> {
                    try {
                        Glide.with(imageView.getContext()).load(urlFuture.get()).into(imageView);
                        imageView.setAlpha(200);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                })).start();
            });
        });
    }

    private void exception() {
        pDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
        pDialog.setTitleText("인터넷 연결상태를 확인해주세요.");
        pDialog.confirmButtonColor(R.color.confirm_button);
        pDialog.setConfirmText("확인");
        pDialog.setConfirmClickListener(d -> {
            pDialog.dismissWithAnimation();
            startActivity(new Intent(this, WifiActivity.class));
        });
    }
}
