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
    private float eyeX;
    private float eyeY;
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
        if(i==1){
            eyes.setImageResource(R.drawable.sad_eye);
            mouse.setImageResource(R.drawable.sad);

        }else if(i==2){
            eyes.setImageResource(R.drawable.oops_eye);
            mouse.setImageResource(R.drawable.smile);
        }else if(i==3){
            eyes.setImageResource(R.drawable.normal_eye);
            mouse.setImageResource(R.drawable.normal);


        }else if(i==4){


        }else if(i==5){


        }else if(i==6){
            i=0;
        }
        i++;


    }
}
