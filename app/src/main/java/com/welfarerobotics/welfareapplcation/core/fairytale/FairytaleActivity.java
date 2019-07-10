package com.welfarerobotics.welfareapplcation.core.fairytale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.entity.FairyTail;
import com.welfarerobotics.welfareapplcation.util.Sound;
import com.welfarerobotics.welfareapplcation.util.TypeWriterView;

public class FairytaleActivity extends BaseActivity {

    private FairyTail fairytail;
    private TypeWriterView writerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//액티비티를 다이얼로그 형식으로 표시
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fairytale);
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        layoutParams.dimAmount = 1.0f; //메인액티비티 투명도 조절
//        layoutParams.alpha = 0.0f;
        writerView =findViewById(R.id.fairy_type);


//        getWindow().setAttributes(layoutParams);
//
//        Display dp = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//        int width = (int) (dp.getWidth() * 1.0);
//        int height = (int) (dp.getHeight() * 1.0);
//        getWindow().getAttributes().width = width;
//        getWindow().getAttributes().height = height;
        Intent intent = getIntent();
        fairytail=FairytailCache.getInstance().getFairytail().get(intent.getIntExtra("item",0));

        FairytaleHandler handler = new FairytaleHandler(writerView);
        writerView.setAuto(true);
        Thread thread = new Thread(() -> FairytaleReader.get(fairytail).play(handler));

        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                FairytaleReader.get(fairytail).stop();
                finish();
                break;
            case MotionEvent.ACTION_UP:    //화면을 터치했다 땠을때
                break;
            case MotionEvent.ACTION_MOVE:    //화면을 터치하고 이동할때
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {

    }

    @Override protected void onResume() {
        super.onResume();
        Sound.get().resume(this, R.raw.fairytale);
        Sound.get().loop(true);
    }

    @Override protected void onPause() {
        super.onPause();
        Sound.get().pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Sound.get().stop();
    }
}
