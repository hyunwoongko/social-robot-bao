package com.welfarerobotics.welfareapplcation.core.greeting;

import android.Manifest;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import com.kinda.alert.KAlertDialog;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.init_setting.InitialSettingActivity;
import com.welfarerobotics.welfareapplcation.databinding.GreetingView;

public class GreetingActivity extends BaseActivity<GreetingView, GreetingViewModel> {

    @Override public void onCreateView(int layout, Class<? extends AndroidViewModel> clazz) {
        super.onCreateView(R.layout.greeting_view, GreetingViewModel.class);
    }


    @Override public void observeData() {
        viewModel.getTypeWriterView().setValue(view.greetingView);
    }

    @Override public void observeAction() {
        viewModel.getShowDialogEvent().observe(this, o -> {
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
        });

        viewModel.getMoveToUserSettingActivityEvent().observe(this, o -> {
            Intent intent = new Intent(GreetingActivity.this, InitialSettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
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