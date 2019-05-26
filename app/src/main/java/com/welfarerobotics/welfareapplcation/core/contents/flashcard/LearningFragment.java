package com.welfarerobotics.welfareapplcation.core.contents.flashcard;

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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.entity.cache.FlashcardCache;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static com.welfarerobotics.welfareapplcation.core.contents.flashcard.FlashcardActivity.index;

public class LearningFragment extends Fragment {
    private LearningFragment learningFragment = null;
    private FlashcardCache cache = FlashcardCache.getInstance();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String[] name = new String[4];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_learning, container, false);

        //xml 변수 생성
        ImageButton ibleftbtn = v.findViewById(R.id.learningleft);
        ImageButton ibrightbtn = v.findViewById(R.id.learningright);
        ImageButton ib1 = v.findViewById(R.id.learning1);
        ImageButton ib2 = v.findViewById(R.id.learning2);
        ImageButton ib3 = v.findViewById(R.id.learning3);
        ImageButton ib4 = v.findViewById(R.id.learning4);
        TextView tvIndex = v.findViewById(R.id.learningIndex);

        //페이지 번호 설정
        tvIndex.setText(String.valueOf(index));

        //이미지뷰 배열 생성
        ImageButton[] ibArray = {ib1, ib2, ib3, ib4};

        //라이브러리 ImageLoader 사용
        ImageLoader imageLoader = ImageLoader.getInstance();

        //페이지 전환에 따른 프래그먼트 생성
        learningFragment = new LearningFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        /* 각 페이지당 표시할 카드 수는 4개
         * 1페이지 : index=1, 카드 범위=0~3, i의 범위=0~3
         * 2페이지 : index=2, 카드 범위=4~7, i의 범위=4~7
         * 6페이지 : index=6, 카드 범위=20~23, i의 범위=20~23
        */
        for (int i = (index-1)*4; i < 4*index; i++) {
            if(i>=cache.getFlashcardSize()){
                break;
            }
            String[] child = cache.getFlashcard(i);
            imageLoader.displayImage(child[0],ibArray[i%4]);
            name[i%4]=child[1];
        }

        //페이지 처음이면 왼쪽 버튼이 invisible
        //페이지 맨끝이면 오른쪽 버튼이 invisible
        if (index == 1) {
            ibleftbtn.setVisibility(View.INVISIBLE);
        }
        if (index == cache.getFlashcardSize()/4) {
            ibrightbtn.setVisibility(View.INVISIBLE);
        }

        //왼쪽 버튼과 오른쪽 버튼 클릭시 페이지 전환
        ibleftbtn.setOnClickListener(view -> {
            --index;
            fragmentTransaction
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.container, learningFragment, "learningSub")
                    .commit();
        });

        ibrightbtn.setOnClickListener(view -> {
            ++index;
            fragmentTransaction
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.container, learningFragment, "learningSub")
                    .commit();
        });

        //각 이미지 클릭시 읽어주기(TTS) 기능
        ib1.setOnClickListener(view -> {
            if(mediaPlayer!=null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
            }
            playVoice(mediaPlayer, name[0]);
        });
        ib2.setOnClickListener(view -> {
            if(mediaPlayer!=null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
            }
            playVoice(mediaPlayer, name[1]);
        });
        ib3.setOnClickListener(view -> {
            if(mediaPlayer!=null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
            }
           playVoice(mediaPlayer, name[2]);
        });
        ib4.setOnClickListener(view -> {
            if(mediaPlayer!=null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
            }
           playVoice(mediaPlayer, name[3]);
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
                    String tempname = "navercssflashcard";
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
        String tempname = "navercssflashcard";
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
