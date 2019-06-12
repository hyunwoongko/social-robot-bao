package com.welfarerobotics.welfareapplcation.bot.face;


import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.util.concurrent.Executors;

/**
 * @author : Hyunwoong
 * @when : 5/26/2019 6:16 PM
 * @homepage : https://github.com/gusdnd852
 */
public class Eye {
    private static String facialExpression = "neutral";
    private static Coordinate faceCoordinates;
    private static float eyeX=0;
    private static float eyeY=0;
    private static Eye eye;
    private final float weightX = 1.0f;
    private final float weightY = 0.5f;
    private static Handler sightHandler = new Handler();
    public static Eye getEye(){
        if(eye==null){
            eye = new Eye();

        }
        return  eye;

    }

    public void seeEmotion(String emotion){
        facialExpression = emotion;

    }

    public String getFacialExpression(){

        return  facialExpression;
    }
    public void seeCoordinate( Coordinate faceCoordinates){
        eyeX = ((faceCoordinates.getLeft()))*weightX;
        eyeY = ((faceCoordinates.getTop()))*weightY;
        Log.d("눈 확인","eyeX:"+eyeX+"  eyeY"+eyeY);

    }

    public void see(String facialExpression, Coordinate faceCoordinates) {
        Eye.facialExpression = facialExpression;
        Eye.faceCoordinates = faceCoordinates;


    }

    public static float getEyeX() {
        return eyeX;
    }

    public static float getEyeY() {
        return eyeY;
    }

    public static void setEye(ImageView iv, float x, float speed) {
        Executors.newSingleThreadExecutor().execute(() -> {
            float weight = 100;
            if (x > iv.getX()) {
                for (float ix = iv.getX(); ix < x; ix += 0.5) {
                    float finalIx = ix;
                    weight += speed;
                    sightHandler.postDelayed(() -> iv.setX(finalIx), (long) weight);
                }
            } else {
                for (float ix = iv.getX(); ix > x; ix -= 0.5) {
                    float finalIx = ix;
                    weight += speed;
                    sightHandler.postDelayed(() -> iv.setX(finalIx), (long) weight);
                }
            }
        });
    }

}
