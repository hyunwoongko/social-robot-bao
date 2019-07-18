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
    /*
    *
    RHOMBUS("R"),PARALLELOGRAM("P"),TRIANGLE1("T1"),TRIANGLE2("T2"),TRIANGLE3("T3"),TRIANGLE4("T4"),TRIANGLE5("T5"),TRIANGLE1_90("T190"),TRIANGLE1_180("T1180"),TRIANGLE1_270("T1270"),TRIANGLE2_90("T290"),
    TRIANGLE2_180("T2180"),TRIANGLE2_270("T2270"),TRIANGLE3_90("T390"),TRIANGLE3_180("T3180"),TRIANGLE3_270("T3270"),
    TRIANGLE4_90("T490"),TRIANGLE4_180("T4180"),TRIANGLE4_270("T4270"),TRIANGLE5_90("T590"),TRIANGLE5_180("T5180"),
    TRIANGLE5_270("T5270"),PARALLELOGRAM_90("P90");
    *
    *
    * */

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


