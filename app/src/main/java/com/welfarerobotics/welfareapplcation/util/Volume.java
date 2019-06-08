package com.welfarerobotics.welfareapplcation.util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.widget.Toast;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;

import static android.content.Context.AUDIO_SERVICE;


public class Volume {
    private AudioManager am = null;

    // 볼륨 낮추는 메소드
    public void volumeDown(Activity activity) {
        am = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        // 현재 볼륨 가져오기
        int volume = am.getStreamVolume(AudioManager.STREAM_MUSIC); //volume은 0~15 사이어야 함
        // volume이 0보다 클 때만 키우기 동작
        if (volume > 3) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, volume - 3, AudioManager.FLAG_PLAY_SOUND);
        } else {
            Brain.hippocampus.decideToSay("이미 소리가 작은 것 같은데요.");
        }
    }

    // 볼륨 높이는 메소드
    public void volumeUp(Activity activity) {
        AudioManager am = (AudioManager) activity.getSystemService(AUDIO_SERVICE);
        // 현재 볼륨 가져오기
        int volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        // volume이 15보다 작을 때만 키우기 동작
        if (volume < 15) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, volume + 3, AudioManager.FLAG_PLAY_SOUND);
        } else {
            Brain.hippocampus.decideToSay("이미 소리가 큰 것 같은데요.");
        }
    }
}