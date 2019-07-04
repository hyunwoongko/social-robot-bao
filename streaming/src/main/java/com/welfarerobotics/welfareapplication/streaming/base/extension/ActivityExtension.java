package com.welfarerobotics.welfareapplication.streaming.base.extension;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import com.welfarerobotics.welfareapplication.streaming.R;
import com.welfarerobotics.welfareapplication.streaming.util.sound.Sound;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 12:54
 * @homepage : https://github.com/gusdnd852
 * <p>
 * Activity 기능 확장
 */
public abstract class ActivityExtension extends AppCompatActivity {

    private int song = -1;
    private Handler handler = new Handler();

    protected void setBackgroundMusic(int resid) {
        this.song = song;
    }

    protected void getPermission(String... permisions) {
        ActivityCompat.requestPermissions(this, permisions, 1);
    }

    public void startMusic() {
        if (song != -1) Sound.get().resume(this, song);
    }

    public void pauseMusic() {
        if (song != -1) Sound.get().pause();
    }

    @Override protected void onResume() {
        super.onResume();
        startMusic();
    }

    @Override protected void onPause() {
        super.onPause();
        pauseMusic();
    }

    @Override public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    @Override public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    public void startActivity(Class<? extends Activity> activity) {
        startActivity(new Intent(this, activity));
    }

    public void startActivityAndFinish(Class<? extends Activity> activity) {
        startActivity(activity);
        finish();
    }

    public Handler getHandler() {
        return handler;
    }
}
