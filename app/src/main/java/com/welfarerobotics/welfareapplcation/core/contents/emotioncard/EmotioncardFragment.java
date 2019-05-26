package com.welfarerobotics.welfareapplcation.core.contents.emotioncard;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.entity.cache.EmotioncardCache;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static com.welfarerobotics.welfareapplcation.core.contents.emotioncard.EmotioncardActivity.emotionIndex;

public class EmotioncardFragment extends Fragment {
    private EmotioncardFragment emotioncardFragment = null;
    private EmotioncardCache cache = EmotioncardCache.getInstance();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String[] name = new String[2];

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_emotioncard, container, false);

        ImageButton ibleftbtn = v.findViewById(R.id.emotionleft);
        ImageButton ibrightbtn = v.findViewById(R.id.emotionright);
        ImageButton ibEmotion1 = v.findViewById(R.id.emotion1);
        ImageButton ibEmotion2 = v.findViewById(R.id.emotion2);
        TextView tvIndex = v.findViewById(R.id.emotionIndex);

        tvIndex.setText(String.valueOf(emotionIndex));

        ImageButton[] ibArray = {ibEmotion1, ibEmotion2};

        //라이브러리 ImageLoader 사용
        ImageLoader imageLoader = ImageLoader.getInstance();

        //페이지 전환에 따른 프래그먼트 생성
        emotioncardFragment = new EmotioncardFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        for (int i = (emotionIndex - 1) * 2; i < 2 * emotionIndex; i++) {
            if (i >= cache.getEmotioncardSize()) {
                break;
            }
            String[] child = cache.getEmotioncard(i);
            Glide
                    .with(this)
                    .load(child[0])
                    .apply(new RequestOptions().override(600, 850).fitCenter())
                    .into(ibArray[i % 2]);
            //imageLoader.displayImage(child[0],ibArray[i%2]);
            name[i % 2] = child[1];
        }

        //페이지 처음이면 왼쪽 버튼이 invisible
        //페이지 맨끝이면 오른쪽 버튼이 invisible
        if (emotionIndex == 1) {
            ibleftbtn.setVisibility(View.INVISIBLE);
        }
        if (emotionIndex == cache.getEmotioncardSize() / 2) {
            ibrightbtn.setVisibility(View.INVISIBLE);
        }

        //왼쪽 버튼과 오른쪽 버튼 클릭시 페이지 전환
        ibleftbtn.setOnClickListener(view -> {
            --emotionIndex;
            fragmentTransaction
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.container, emotioncardFragment)
                    .commit();
        });

        ibrightbtn.setOnClickListener(view -> {
            ++emotionIndex;
            fragmentTransaction
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.container, emotioncardFragment)
                    .commit();
        });

        //각 이미지 클릭시 읽어주기(TTS) 기능
        ibEmotion1.setOnClickListener(view -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            playVoice(mediaPlayer, name[0]);
        });

        ibEmotion2.setOnClickListener(view -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            playVoice(mediaPlayer, name[1]);
        });

        return v;
    }

    private void playVoice(MediaPlayer mediaPlayer, String tts) {
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
                String postParams = "speaker=jinho&speed=2.5&text=" + text;
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
        String tempname = "navercssemotioncard";
        String Path_to_file = Environment.getExternalStorageDirectory() +
                File.separator + "NaverCSS/" + tempname + ".mp3";

        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
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
}
