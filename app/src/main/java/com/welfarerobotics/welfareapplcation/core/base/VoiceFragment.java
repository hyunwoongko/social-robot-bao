package com.welfarerobotics.welfareapplcation.core.base;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechPresignRequest;
import com.welfarerobotics.welfareapplcation.bot.SSML;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import com.welfarerobotics.welfareapplcation.util.AwsPollyClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author : Hyunwoong
 * @when : 6/9/2019 11:40 AM
 * @homepage : https://github.com/gusdnd852
 */
public class VoiceFragment extends Fragment {
    protected void playVoice(MediaPlayer mediaPlayer, String tts) {
        /*
         * {Gender: Female,Id: Seoyeon,LanguageCode: ko-KR,LanguageName: Korean,Name: Seoyeon,}
         * */
        SynthesizeSpeechPresignRequest request = new SynthesizeSpeechPresignRequest()
                .withText(SSML.setSSML(tts))
                .withTextType("ssml")
                .withVoiceId("Seoyeon")
                .withOutputFormat(OutputFormat.Mp3);

        URL url = AwsPollyClient.getInstance(getContext())
                .getClient()
                .getPresignedSynthesizeSpeechUrl(request);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            // Set media player's data source to previously obtained URL.
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url.toString());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
