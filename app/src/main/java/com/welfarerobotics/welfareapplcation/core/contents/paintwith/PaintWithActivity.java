package com.welfarerobotics.welfareapplcation.core.contents.paintwith;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kinda.alert.KAlertDialog;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.settings.WifiActivity;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import com.welfarerobotics.welfareapplcation.util.CanvasView;
import com.welfarerobotics.welfareapplcation.util.DeviceId;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

/**
 * @Author : Hyunwoong
 * @When : 4/9/2019 1:51 AM
 * @Homepage : https://github.com/gusdnd852
 */
public class PaintWithActivity extends BaseActivity {

    private CanvasView canvasView;
    private String uuid;
    private String first = "true";

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
            KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
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
            uploadTask.addOnFailureListener(exception -> {
                pDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                pDialog.setTitleText("인터넷 연결상태를 확인해주세요.");
                pDialog.confirmButtonColor(R.color.confirm_button);
                pDialog.setConfirmText("확인");
                pDialog.setConfirmClickListener(d -> {
                    pDialog.dismissWithAnimation();
                    startActivity(new Intent(this, WifiActivity.class));
                });
            }).addOnSuccessListener(taskSnapshot -> {
                RequestOptions options = RequestOptions
                        .timeoutOf(5 * 60 * 1000)
                        .fitCenter()
                        .signature(new ObjectKey(System.currentTimeMillis()))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(false);

                Glide.with(imageView.getContext())
                        .load(ServerCache.getInstance().getPainter() + "/draw/" + uuid + "/" + first + "/" + taskSnapshot.getDownloadUrl().toString().split("\\?")[0])
                        .listener(new RequestListener<Drawable>() {
                            @Override public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                pDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                                pDialog.setTitleText("인터넷 연결상태를 확인해주세요.");
                                pDialog.confirmButtonColor(R.color.confirm_button);
                                pDialog.setConfirmText("확인");
                                pDialog.setConfirmClickListener(d -> {
                                    pDialog.dismissWithAnimation();
                                    startActivity(new Intent(PaintWithActivity.this, WifiActivity.class));
                                });
                                return false;
                            }

                            @Override public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                pDialog.dismissWithAnimation();
                                return false;
                            }
                        })
                        .apply(options)
                        .into(imageView);
                imageView.setAlpha(185);
                first = "false";
            });
        });
    }
}
