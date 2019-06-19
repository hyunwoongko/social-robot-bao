package com.welfarerobotics.welfareapplcation.bot.face;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.welfarerobotics.welfareapplcation.R;


public class FaceHandler extends Handler {
    private ImageView eyes;
    private ImageView mouse;
    private  TextView emotion;
    private  float eyeX;
    private  float eyeY;
    private int i=0;
    private Activity activity;

    public FaceHandler(ImageView eyes, TextView emotion, ImageView mouse, Activity activity){
        this.activity = activity;
        this.eyes = eyes;
        this.mouse=mouse;
        this.emotion=emotion;
        eyeX =eyes.getX();
        eyeY=eyes.getY();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
       // Log.d("핸들러 작동 테스트:","테스트중"+Eye.getEye().getEyeX()+" "+ Eye.getEye().getEyeY()+" ");

        emotion.setText(Eye.getEye(activity).getFacialExpression());
        if(Eye.getEye(activity).getFacialExpression().equals("None")){
            eyes.setX(eyeX);
            eyes.setY(eyeY);
        }



    }
}
