package com.welfarerobotics.welfareapplcation.bot;

import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import lombok.Data;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public final @Data class Mouth {
    private static Mouth api = null;
    private MediaPlayer audioPlayer = new MediaPlayer();
    private String speaker = "jinho";

    private Mouth() {

    }

    public synchronized static Mouth get() {
        if (api == null) api = new Mouth();
        return api;
    }

    public void say() {
        this.play(Brain.hippocampus.getThoughtSentence());
    }

    public Mouth play(String tts) {
        try {
            String text = URLEncoder.encode(tts, "UTF-8"); // 13자
            String apiURL = "https://naveropenapi.apigw.ntruss.com/voice/v1/tts";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", ServerCache.getInstance().getCssid());
            con.setRequestProperty("X-NCP-APIGW-API-KEY", ServerCache.getInstance().getCsssecret());
            // post request
            String postParams = "speaker=" + speaker + "&speed=4.0&text=" + text;
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
                String tempname = "navercssfile";
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

        String tempname = "navercssfile";
        String Path_to_file = Environment.getExternalStorageDirectory() +
                File.separator + "NaverCSS/" + tempname + ".mp3";

        audioPlayer = new MediaPlayer();

        try {
            audioPlayer.setDataSource(Path_to_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            audioPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioPlayer.start();
        setSpeaker("jinho"); // 다시 진호로 바꿈
        return this;
    }


    public void stop(Runnable nextAction) {
        audioPlayer.setOnCompletionListener(mediaPlayer -> {
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(nextAction, 300);
            audioPlayer.release();
        });
    }

    public void cancel() {
        audioPlayer.release();
    }
}