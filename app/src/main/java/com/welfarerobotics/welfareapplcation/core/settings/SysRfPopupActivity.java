package com.welfarerobotics.welfareapplcation.core.settings;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.initial.SplashActivity;
import com.welfarerobotics.welfareapplcation.util.DeviceId;
import com.welfarerobotics.welfareapplcation.util.data_util.Preference;
import es.dmoral.toasty.Toasty;

import java.util.Random;

public class SysRfPopupActivity extends Activity {
    TextView txtText;
    private int randomint[] = new int[4];
    private int arrayint[] = new int[4];
    private Button confirmbtn;
    private Button cancelbtn;
    private EditText editText;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sysrfpopup);

        userid = DeviceId.getInstance(this).getUUID();
        editText = (EditText) findViewById(R.id.name_editext);
        RandomIntNumber();
        //UI 객체생성
        txtText = (TextView) findViewById(R.id.ramnum_textview);
        //데이터 가져오기
        txtText.setText("다음 숫자 " + randomint[0] + "," + randomint[1] + "," + randomint[2] + "," + randomint[3] + "중 두번째로 큰수를 입력하세요 ");
        confirmbtn = (Button) findViewById(R.id.confirm_button);
        confirmbtn.setOnClickListener(view -> {
            try {
                int input = Integer.parseInt(editText.getText().toString().replace(" ", ""));
                if (input == arrayint[2]) {
                    realtimereset();
                } else {
                    Toasty.info(this, "입력하신 숫자가 아닙니다.").show();
                }
            } catch (NumberFormatException e) {
                Toasty.error(this, "숫자만 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        });
        cancelbtn = (Button) findViewById(R.id.cancel_button);
        cancelbtn.setOnClickListener(view -> {
            finish();
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    private void realtimereset() {
        Preference.get(this).setBoolean("isFirst", true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("user").child(userid);
        databaseRef.setValue(null);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                refresh();
                finish();
                System.out.println("리얼타임삭제완료");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to refresh Bao", databaseError.toException());
            }
        });
    }

    private void RandomIntNumber() {
        Random r = new Random(); //객체생성
        for (int i = 0; i <= 3; i++)    //숫자 6개를 뽑기위한 for문
        {
            randomint[i] = r.nextInt(10) + 1; //1~10숫자중 랜덤으로 하나를 뽑아 a[0]~a[3]에 저장
            for (int j = 0; j < i; j++) //중복제거를 위한 for문
            {
                if (randomint[i] == randomint[j]) {
                    i--;
                }
            }
            arrayint[0] = randomint[0];
            arrayint[1] = randomint[1];
            arrayint[2] = randomint[2];
            arrayint[3] = randomint[3];
        }

        for (int i = 0; i <= 3; i++) {
            for (int j = i + 1; j <= 3; j++) {
                if (arrayint[i] > arrayint[j]) {
                    int temp = arrayint[i];
                    arrayint[i] = arrayint[j];
                    arrayint[j] = temp;
                }
            }
        }
    }

    private void refresh() {
        Intent mStartActivity = new Intent(this, SplashActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10, mPendingIntent);
        finishAffinity();
        System.runFinalization();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}