package com.welfarerobotics.welfareapplcation.core.initial;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import com.kinda.alert.KAlertDialog;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.base.VoiceActivity;
import com.welfarerobotics.welfareapplcation.util.Sound;
import com.welfarerobotics.welfareapplcation.util.TypeWriterView;

public class GreetingActivity extends BaseActivity {

    private int msgCount = 0;
    private String[] msg = {
            "안녕하세요.",
            "저는 인공지능 로봇 바오라고 합니다.",
            "원활한 사용을 위해서 블루투스 및 와이파이 설정과",
            "간단한 사용자 정보를 입력하는 과정을 거쳐야 합니다.",
            "입력받은 모든 정보는 안전하게 관리됩니다.",
            "그럼 시작해볼까요?"
    };


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);
        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("권한 승인");
        pDialog.setContentText("\n원할한 사용을 위해 몇가지 권한을 승인해주세요.\n\n");
        pDialog.setConfirmText("확인");
        pDialog.confirmButtonColor(R.color.confirm_button);
        pDialog.setConfirmClickListener(kAlertDialog -> {
            getPermission();
            kAlertDialog.dismissWithAnimation();
        });
        pDialog.setCancelable(false);
        pDialog.show();
        TypeWriterView writerView = findViewById(R.id.greeting_view);
        writerView.setText(msg[msgCount]);

        findViewById(R.id.greeting_next_button).setOnClickListener(v -> {
            msgCount++; // 대화 카운트 증가
            Sound.get().effectSound(getApplicationContext(), R.raw.click);
            if (msgCount >= msg.length) {
                Intent intent = new Intent(GreetingActivity.this, InitialBluetoothActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                writerView.write(msg[msgCount], 70);
            }
        });
    }


    private void getPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_ADMIN}, 1);
    }
}