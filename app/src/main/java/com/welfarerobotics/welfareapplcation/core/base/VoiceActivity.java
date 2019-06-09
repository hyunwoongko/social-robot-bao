package com.welfarerobotics.welfareapplcation.core.base;

import android.media.MediaPlayer;
import android.os.Environment;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author : Hyunwoong
 * @when : 6/9/2019 11:36 AM
 * @homepage : https://github.com/gusdnd852
 */
public abstract class VoiceActivity extends BaseActivity {
    protected void playVoice(MediaPlayer mediaPlayer, String tts) {
        Thread thread = new Thread(() -> {
            try {
                String text = URLEncoder.encode(tts, "UTF-8"); // 13자
                String apiURL = "https://naveropenapi.apigw.ntruss.com/voice/v1/tts";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", ServerCache.getInstance().getCssid());
                con.setRequestProperty("X-NCP-APIGW-API-KEY", ServerCache.getInstance().getCsssecret());
                // post request
                String postParams = "speaker=jinho&speed=3.5&text=" + text;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    InputStream is = con.getInputStream();
                    int read = 0;
                    byte[] bytes = new byte[1024];
                    //NaverCSS 폴더 생성
                    File dir = new File(Environment.getExternalStorageDirectory() + "/", "NaverCSS");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    // 랜덤한 이름으로 mp3 파일 생성
                    String tempname = "navercssemotioncard";
                    File f = new File(Environment.getExternalStorageDirectory() +
                            File.separator + "NaverCSS/" + tempname + ".mp3");
                    f.createNewFile();
                    OutputStream outputStream = new FileOutputStream(f);
                    while ((read = is.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }
                    is.close();

                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    System.out.println(response.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
        }

        String tempname = "navercssemotioncard";
        String Path_to_file = Environment.getExternalStorageDirectory() +
                File.separator + "NaverCSS/" + tempname + ".mp3";

        try {
            mediaPlayer.setDataSource(Path_to_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }
}
