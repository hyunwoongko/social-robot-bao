package com.welfarerobotics.welfareapplcation.bot;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import com.amazonaws.services.polly.model.*;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import com.welfarerobotics.welfareapplcation.util.AwsPollyClient;
import lombok.Data;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public final @Data class Mouth {
    private static Mouth api = null;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String speaker = "Seoyeon";
    private Runnable r;

    private Mouth() {

    }

    public synchronized static Mouth get() {
        if (api == null) api = new Mouth();
        return api;
    }

    public void say(Context context) {
        this.play(context, Brain.hippocampus.getThoughtSentence());
    }

    public Mouth play(MediaPlayer mediaPlayer, Context context, String tts) {
        /*
         * {Gender: Female,Id: Seoyeon,LanguageCode: ko-KR,LanguageName: Korean,Name: Seoyeon,}
         * */

        SynthesizeSpeechPresignRequest request = new SynthesizeSpeechPresignRequest()
                .withText(SSML.setSSML(tts))
                .withTextType("ssml")
                .withVoiceId("Seoyeon")
                .withOutputFormat(OutputFormat.Mp3);

        URL url = AwsPollyClient.getInstance(context)
                .getClient()
                .getPresignedSynthesizeSpeechUrl(request);

        setSpeaker("Seoyeon");
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            // Set media player's data source to previously obtained URL.
            mediaPlayer.setDataSource(url.toString());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Mouth play(Context context, String tts) {
        /*
         * {Gender: Female,Id: Seoyeon,LanguageCode: ko-KR,LanguageName: Korean,Name: Seoyeon,}
         * */

        SynthesizeSpeechPresignRequest request = new SynthesizeSpeechPresignRequest()
                .withText(SSML.setSSML(tts))
                .withTextType("ssml")
                .withVoiceId(speaker)
                .withOutputFormat(OutputFormat.Mp3);

        URL url = AwsPollyClient.getInstance(context)
                .getClient()
                .getPresignedSynthesizeSpeechUrl(request);

        setSpeaker("Seoyeon");

        try {
            // Set media player's data source to previously obtained URL.
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url.toString());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void stop(Runnable nextAction) {
        r = nextAction;
        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(nextAction, 300);
            mediaPlayer.release();
        });
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(nextAction, 300);
            mediaPlayer.release();
            return false;
        });
    }

    public void cancel() {
        mediaPlayer.release();
    }

}