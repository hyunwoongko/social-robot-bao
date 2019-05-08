package com.welfarerobotics.welfareapplcation.core.contents.tangram;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.welfarerobotics.welfareapplcation.R;


public class Images {

    private static Images images = null;
    private Bitmap triangles[] = new Bitmap[5];
    private Bitmap parallelo;
    private Bitmap rhombus;

    public static Images getImages() {

        if (images != null) {

            return images;
        } else {
            images = new Images();
            return images;

        }

    }

    private Images() {

        triangles[0] = BitmapFactory.decodeResource(TangramActivity.r, R.drawable.triangle1);
        triangles[1] = BitmapFactory.decodeResource(TangramActivity.r, R.drawable.triangle2);
        triangles[2] = BitmapFactory.decodeResource(TangramActivity.r, R.drawable.triangle3);
        triangles[3] = BitmapFactory.decodeResource(TangramActivity.r, R.drawable.triangle4);
        triangles[4] = BitmapFactory.decodeResource(TangramActivity.r, R.drawable.triangle5);
        parallelo = BitmapFactory.decodeResource(TangramActivity.r,R.drawable.parallelogram);
        rhombus = BitmapFactory.decodeResource(TangramActivity.r,R.drawable.rhombus);

    }

    public Bitmap getTriAngle(int type){


        return triangles[type];
    }

    public Bitmap getParallelo(){


        return  parallelo;
    }

    public Bitmap getRhombus(){


        return  rhombus;
    }

}


