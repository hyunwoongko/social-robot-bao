package com.welfarerobotics.welfareapplcation.bot.face;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.util.Pool;

import java.util.concurrent.Executors;

/**
 * @author : Hyunwoong
 * @when : 5/26/2019 6:16 PM
 * @homepage : https://github.com/gusdnd852
 */
public class Eye {
    private static String facialExpression = "neutrall";
    private static Coordinate faceCoordinates;
    private static float eyeX=0;
    private static float eyeY=0;
    private static Eye eye;
    private final float weightX = 1.0f;
    private final float weightY = 0.5f;
    private static Handler sightHandler = new Handler();
    private Activity activity;

    public Eye(Activity activity) {
        this.activity = activity;
    }

    public static Eye getEye(Activity activity){
        if(eye==null){
            eye = new Eye(activity);
        }
        return  eye;
    }

    public void seeEmotion(String emotion){
        facialExpression = emotion;
    }

    public String getFacialExpression(){
        return  facialExpression;
    }

    public void see(float x, float speed) {
        Pool.eyeThread.execute(() -> {
            float weight = 100;
            if (x > activity.findViewById(R.id.eye).getX()) {
                for (float ix = activity.findViewById(R.id.eye).getX(); ix < x; ix += 0.5) {
                    float finalIx = ix;
                    weight += speed;
                    sightHandler.postDelayed(() -> activity.findViewById(R.id.eye).setX(finalIx), (long) weight);
                }
            } else {
                for (float ix = activity.findViewById(R.id.eye).getX(); ix > x; ix -= 0.5) {
                    float finalIx = ix;
                    weight += speed;
                    sightHandler.postDelayed(() -> activity.findViewById(R.id.eye).setX(finalIx), (long) weight);
                }
            }
        });
    }

}
