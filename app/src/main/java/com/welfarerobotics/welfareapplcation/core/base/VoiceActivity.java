package com.welfarerobotics.welfareapplcation.core.base;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechPresignRequest;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.SSML;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import com.welfarerobotics.welfareapplcation.util.AwsPollyClient;
import com.welfarerobotics.welfareapplcation.util.Pool;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author : Hyunwoong
 * @when : 6/9/2019 11:36 AM
 * @homepage : https://github.com/gusdnd852
 */
public abstract class VoiceActivity extends BaseActivity {
    protected void playVoice(MediaPlayer mediaPlayer, String tts) {

        /*
         * {Gender: Female,Id: Seoyeon,LanguageCode: ko-KR,LanguageName: Korean,Name: Seoyeon,}
         * */
        Future<URL> urlFuture = Pool.mouthThread.submit(() -> {

            SynthesizeSpeechPresignRequest request = new SynthesizeSpeechPresignRequest()
                    .withText(SSML.setSSML(tts))
                    .withTextType("ssml")
                    .withVoiceId("Seoyeon")
                    .withOutputFormat(OutputFormat.Mp3);

            return AwsPollyClient.getInstance(this)
                    .getClient()
                    .getPresignedSynthesizeSpeechUrl(request);
        });
        try{
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }catch (Exception e){
            // 이거 연타 되면 에러 뜨는거 같아서 예외처리 해둠.
        }


        try {
            // Set media player's data source to previously obtained URL.
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.setDataSource(urlFuture.get().toString());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e){
            // java.lang.IllegalStateException
            //        at android.media.MediaPlayer.isPlaying(Native Method) 연타시에 이 엑셉션이 뜸. 그래서 그냥 전체 예외처리 해뒀다.
            e.printStackTrace();

        }
    }
}
