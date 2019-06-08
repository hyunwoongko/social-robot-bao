package com.welfarerobotics.welfareapplcation.util.touch_util;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.view.MotionEvent;
import android.view.View;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.ear.EarSet;
import com.welfarerobotics.welfareapplcation.core.main.MainActivity;
import com.welfarerobotics.welfareapplcation.core.menu.MenuActivity;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 1:13 PM
 * @homepage : https://github.com/gusdnd852
 */
public class ConcreteSwipeTouchListener extends OnSwipeTouchListener {
    private AudioManager audioManager;
    private Activity activity;
    private Runnable singleTap;
    public ConcreteSwipeTouchListener(Activity activity, AudioManager audioManager, Runnable singleTap) {
        super(activity);
        this.audioManager = audioManager;
        this.activity = activity;
        this.singleTap = singleTap;
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
        singleTap.run();
    }
}
