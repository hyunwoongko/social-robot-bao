package com.welfarerobotics.welfareapplcation.core.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.firebase.database.*;
import com.kinda.alert.KAlertDialog;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.entity.Quiz;
import com.welfarerobotics.welfareapplcation.entity.User;
import com.welfarerobotics.welfareapplcation.entity.cache.UserCache;
import com.welfarerobotics.welfareapplcation.util.DeviceId;
import com.welfarerobotics.welfareapplcation.util.ToastType;
import com.welfarerobotics.welfareapplcation.util.data_util.Preference;

import java.util.Random;

public class SysRefreshActivity extends BaseActivity {
    private Toolbar toolbar;
    private ImageView baoimg;
    private Button changebtn;
    private  ImageView backimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_refresh);
//        toolbar = findViewById(R.id.con_menu);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("시스템 초기화");
        backimg = (ImageView) findViewById(R.id.backview);
        backimg.setOnClickListener(view -> {
            finish();
        });
        changebtn = (Button) findViewById(R.id.change_button);
        changebtn.setOnClickListener(view -> {
            String id = DeviceId.getInstance(this).getUUID();
            User model = new User();
            model.setId(id);

            EditText nameEditText = findViewById(R.id.name_editext);
            EditText addressEditText = findViewById(R.id.region_editext);

            if (nameEditText.getText() != null &&
                    !nameEditText.getText().toString().trim().equals("") &&
                    !nameEditText.getText().toString().replaceAll(" ", "").equals("") &&
                    addressEditText.getText() != null &&
                    !addressEditText.getText().toString().trim().equals("") &&
                    !addressEditText.getText().toString().replaceAll(" ", "").equals("")) {

                model.setName(nameEditText.getText().toString());
                model.setLocation(addressEditText.getText().toString());
                model.setPhoto(model.getPhoto());
                UserCache.setInstance(model);
                // 싱글톤 업로드

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseRef = database.getReference("user").child(id);
                databaseRef.setValue(model);
                KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("이름, 지역 재설정중...");
                pDialog.setCancelable(false);
                pDialog.show();
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        pDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                        pDialog.setTitleText("이름, 지역을 재설정하였습니다");
                        pDialog.confirmButtonColor(R.color.confirm_button);
                        pDialog.setConfirmText("확인");
                        pDialog.setConfirmClickListener(KAlertDialog::dismissWithAnimation);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TAG: ", "Failed to refresh Bao", databaseError.toException());
                        pDialog.dismissWithAnimation();
                        showToast("이름,지역 재설정을 실패하였습니다. 연결상태를 확인해주세요.", ToastType.error);
                    }
                });

            }
        });
        baoimg = (ImageView) findViewById(R.id.Bao_view);
        baoimg.setOnClickListener(view -> {
            startActivity(new Intent(this, SysRfPopupActivity.class));
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
