package com.welfarerobotics.welfareapplcation.bot.face;

import android.app.Activity;
import android.media.Image;
import android.widget.ImageView;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.bot.brain.Hormone;
import com.welfarerobotics.welfareapplcation.bot.brain.Pituitary;

/**
 * @author : Hyunwoong
 * @when : 2019-07-01 오후 6:51
 * @homepage : https://github.com/gusdnd852
 */
public class FaceExpressionGenerator {
    public static void ganerate(Activity activity, Hormone hormone, ImageView eye, ImageView mouth){
        activity.runOnUiThread(() -> {
            switch (hormone) {
                case Dopamine:
                    eye.setImageResource(R.drawable.very_happy_eye);
                    mouth.setImageResource(R.drawable.very_happy_mouse);
                    break;
                case Endorphin:
                    eye.setImageResource(R.drawable.happy_eye);
                    mouth.setImageResource(R.drawable.normal_mouse);
                    break;
                case Serotonin:
                    eye.setImageResource(R.drawable.normal_eye);
                    mouth.setImageResource(R.drawable.normal_mouse);
                    break;
                case Cortisol:
                    eye.setImageResource(R.drawable.sad_sad_eye);
                    mouth.setImageResource(R.drawable.depressed_mouse);
                    break;
                case Noradrenalin:
                    eye.setImageResource(R.drawable.very_sad_eye);
                    mouth.setImageResource(R.drawable.depressed_mouse);
                    break;
                default:
                    eye.setImageResource(R.drawable.normal_eye);
                    mouth.setImageResource(R.drawable.normal_mouse);
            }
        });
    }
}
