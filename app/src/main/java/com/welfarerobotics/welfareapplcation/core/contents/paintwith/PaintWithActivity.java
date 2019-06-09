package com.welfarerobotics.welfareapplcation.core.contents.paintwith;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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
import com.welfarerobotics.welfareapplcation.core.base.VoiceActivity;
import com.welfarerobotics.welfareapplcation.core.settings.WifiActivity;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import com.welfarerobotics.welfareapplcation.entity.cache.UserCache;
import com.welfarerobotics.welfareapplcation.util.CanvasView;
import com.welfarerobotics.welfareapplcation.util.DeviceId;
import com.welfarerobotics.welfareapplcation.util.Sound;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.Random;

/**
 * @Author : Hyunwoong
 * @When : 4/9/2019 1:51 AM
 * @Homepage : https://github.com/gusdnd852
 */
public class PaintWithActivity extends VoiceActivity {

    private CanvasView canvasView;
    private String uuid;
    private String first = "true";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Random random = new Random();
    private String[] readyStrings = {
            "우와 정말 잘 그리셨어요. 저도 한번 그려볼게요 !",
            UserCache.getInstance().getName() + "님, 정말 잘 그렸어요. 이번엔 제가 그려볼게요!",
            "우와 " + UserCache.getInstance().getName() + "님, 그림실력이 대단한데요.. 이번엔 제가 그려볼게요!"
    };

    private String[] resultStrings = {
            "저는 이렇게 그려봤는데 어떤가요?",
            "저는 이런 느낌으로 그려 봤어요! 이런 느낌은 어떤가요?",
            "이렇게도 그릴 수 있답니다. 물론 저는 " + UserCache.getInstance().getName() + "님 보다는 못 그리지만요"
    };

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_with);
        canvasView = findViewById(R.id.canvas_view);
        canvasView.setBackgroundColor(Color.TRANSPARENT);

        uuid = DeviceId.getInstance(this).getUUID();
        findViewById(R.id.redo_button).setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, "앞으로 되돌리기");
            canvasView.redo();
        });
        findViewById(R.id.undo_button).setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, "뒤로 되돌리기");
            canvasView.undo();
        });
        findViewById(R.id.clear_button).setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, "모두 지우기");
            canvasView.clear();
        });
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        ImageView imageView = findViewById(R.id.paint_with_image);


        findViewById(R.id.pencil_button).setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, "연필");
            this.canvasView.setPaintStrokeWidth(5F);
            this.canvasView.setMode(CanvasView.Mode.DRAW);    // for drawing
        });
        findViewById(R.id.eraser_button).setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, "지우개");
            this.canvasView.setPaintStrokeWidth(40F);
            this.canvasView.setMode(CanvasView.Mode.ERASER);  // for using Eraser
        });

        findViewById(R.id.style_button).setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, readyStrings[random.nextInt(readyStrings.length-1)]);

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
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                mediaPlayer = new MediaPlayer();
                                playVoice(mediaPlayer, resultStrings[random.nextInt(resultStrings.length-1)]);
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

    @Override protected void onResume() {
        super.onResume();
        Sound.get().resume(this, R.raw.paint);
        Sound.get().loop(true);
    }

    @Override protected void onPause() {
        super.onPause();
        Sound.get().pause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        Sound.get().stop();
    }
}
