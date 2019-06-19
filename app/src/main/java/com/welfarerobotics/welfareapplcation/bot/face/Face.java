package com.welfarerobotics.welfareapplcation.bot.face;

import android.widget.ImageView;

/**
 * @author : Hyunwoong
 * @when : 5/27/2019 6:34 PM
 * @homepage : https://github.com/gusdnd852
 */

public class Face {
    static Face face = null;
    private ImageView eyes;
    private ImageView mouse;


    public Face getInstance() {
        if (face == null) {

            face = new Face();

        }

        return face;

    }

    private Face() {


    }

    public void setEyes(ImageView eyes) {
        this.eyes = eyes;
    }

    public void setMouse(ImageView mouse) {
        this.mouse = mouse;
    }

    public ImageView getEyes() {
        return eyes;
    }

    public ImageView getMouse() {
        return mouse;
    }


}
