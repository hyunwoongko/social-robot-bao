package com.welfarerobotics.welfareapplcation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

//Activity들의 Base가되는 Activity
public class BaseActivity extends AppCompatActivity {
    public Typeface mTypeface = null;
    @Override
    public void setContentView(int layoutResID){
        super.setContentView(layoutResID);
        if(mTypeface == null){
            mTypeface = Typeface.createFromAsset(this.getAssets(),"font/applegothicreqular");
        }

        setGlobalFont(getWindow().getDecorView());
    }

    private void setGlobalFont(View view){
        if(view != null){
            if(view instanceof ViewGroup){
                ViewGroup vg = (ViewGroup)view;
                int vgCnt = vg.getChildCount();
                for(int i=0; i<vgCnt; i++){
                    View v = vg.getChildAt(i);
                    if(v instanceof TextView){
                        ((TextView)v).setTypeface(mTypeface);
                    }
                    setGlobalFont(v);
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }
    //액션바 터치이벤트
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int X = (int) event.getX();
        int Y = (int) event.getY();
        int eventaction = event.getAction();

        if(Y < 400) {
            onWindowFocusChanged(true);
            //    Toast.makeText(this, "ACTION_DOWN AT COORDS " + "X: " + X + " Y: " + Y, Toast.LENGTH_SHORT).show();
        }


        return true;
    }
    //시스템 다이얼로그 닫기 로직 추가
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Log.d("Focus debug", "Focus changed !");

        if(!hasFocus) {
            Log.d("Focus debug", "Lost focus !");

            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

}
