package com.welfarerobotics.welfareapplcation.core.fairytale;

import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import com.welfarerobotics.welfareapplcation.entity.FairyTail;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import com.welfarerobotics.welfareapplcation.util.TypeWriterView;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public final class FairytaleReader {
    private static FairytaleReader fairytale = null;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean flag;
    private  FairyTail fairyTail;
    private String[]context;

    private FairytaleReader(FairyTail fairyTail) {
        this.fairyTail =fairyTail;
    }


    public synchronized static FairytaleReader get(FairyTail fairyTail) {
        if (fairytale == null) {

            fairytale = new FairytaleReader(fairyTail);

        }else{
            fairytale.setfairyTail(fairyTail);

        }
        return fairytale;
    }

     private void setfairyTail(FairyTail fairyTail){
        this.fairyTail = fairyTail;
    }

    private void playVoice(MediaPlayer mediaPlayer, String tts) {
        try {
            String text = URLEncoder.encode(tts, "UTF-8"); // 13자
            String apiURL = "https://naveropenapi.apigw.ntruss.com/voice/v1/tts";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", ServerCache.getInstance().getCssid());
            con.setRequestProperty("X-NCP-APIGW-API-KEY", ServerCache.getInstance().getCsssecret());
            // post request
            String postParams = "speaker=jinho&speed=3.0&text=" + text;
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
                String tempname = "navercssfairytale";
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

        String tempname = "navercssfairytale";
        String Path_to_file = Environment.getExternalStorageDirectory() +
                File.separator + "NaverCSS/" + tempname + ".mp3";

        try {
            mediaPlayer.setDataSource(Path_to_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    public void play() {


        flag = true;
        String[] select = fairyTail.getContext().split(",");
        mediaPlayer = new MediaPlayer();
        playVoice(mediaPlayer, select[0]);
        int idx = 0;
        while (flag) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        System.out.println(select.length);
                    if (idx < select.length-1) {
                        mediaPlayer.release();
                        mediaPlayer = new MediaPlayer();
                        idx++;
                        playVoice(mediaPlayer, select[idx]);
                    } else {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        flag = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void play(FairytaleHandler writerView) {
        flag = true;
        String[] select = fairyTail.getContext().split(",");
        mediaPlayer = new MediaPlayer();
        playVoice(mediaPlayer, select[0]);
        Message msg =writerView.obtainMessage();
        msg.obj = new String(select[0]);
        writerView.sendMessage(msg);

        int idx = 0;
        while (flag) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    System.out.println(select.length);
                    if (idx < select.length-1) {
                        mediaPlayer.release();
                        mediaPlayer = new MediaPlayer();
                        idx++;
                        final String fairy = select[idx];
                        Message message = writerView.obtainMessage();
                        message.obj=fairy;
                        writerView.sendMessage(message);
                        playVoice(mediaPlayer, select[idx]);




//                        writerView.write(select[idx],70,1);

                    } else {
                        mediaPlayer.stop();
                        mediaPlayer.release();
//                        writerView.write("",70,1);
                        msg=writerView.obtainMessage();
                        msg.obj="";
                        writerView.sendMessage(msg);
                        flag = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void stop() {
        if(flag) {
            try{
                mediaPlayer.stop();
                mediaPlayer.release();
                flag = false;

            }catch (Exception e){
                stop();
            }

        }
    }
}
