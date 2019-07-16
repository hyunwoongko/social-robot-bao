package com.welfarerobotics.welfareapplcation.util.touch_util;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import com.welfarerobotics.welfareapplcation.core.menu.MenuActivity;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 1:13 PM
 * @homepage : https://github.com/gusdnd852
 */
public class ConcreteSwipeTouchListener extends OnSwipeTouchListener {
    private AudioManager audioManager;
    private Activity activity;

    public ConcreteSwipeTouchListener(Activity activity, AudioManager audioManager) {
        super(activity);
        this.audioManager = audioManager;
        this.activity = activity;
    }

    @Override
    public void onSwipeTop() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }

    @Override
    public void onSwipeBottom() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }

    @Override
    public void onActivityDoubleTap() {
        Intent menuIntent = new Intent(activity, MenuActivity.class);
        activity.startActivity(menuIntent);
    }

    @Override public void onActivitySingleTap() {
    //    Intent menuIntent = new Intent(activity, MenuActivity.class);
    //    activity.startActivity(menuIntent);
    }
}
