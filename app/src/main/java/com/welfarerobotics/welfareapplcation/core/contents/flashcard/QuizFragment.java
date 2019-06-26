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
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.entity.cache.FlashcardCache;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static com.welfarerobotics.welfareapplcation.core.contents.flashcard.FlashcardActivity.index;

public class QuizFragment extends Fragment {
    private QuizFragment quizFragment = null;
    private FlashcardCache cache = FlashcardCache.getInstance();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean flag;
    private String name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);
        flag = false;

        //xml 변수 생성
        ImageButton ibQuizImage = v.findViewById(R.id.quizImage);
        ImageButton ibleftbtn = v.findViewById(R.id.quizLeft);
        ImageButton ibrightbtn = v.findViewById(R.id.quizRight);
        TextView tvQuizText = v.findViewById(R.id.quizText);
        TextView tvAnswer = v.findViewById(R.id.quizAnswer);


        //페이지 이동을 위한 변수 생성
        quizFragment = new QuizFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        //각 페이지 별 카드 설정
        String[] child = cache.getFlashcard(index - 1);
        Glide
                .with(this)
                .load(child[0])
                .apply(new RequestOptions().override(225, 319).fitCenter())
                .into(ibQuizImage);
        name = child[1];
        tvQuizText.setText(child[1]);

        //페이지 처음이면 왼쪽 버튼이 invisible
        //페이지 맨끝이면 오른쪽 버튼이 invisible
        if (index == 1) {
            ibleftbtn.setVisibility(View.INVISIBLE);
        }
        if (index == cache.getFlashcardSize()) {
            ibrightbtn.setVisibility(View.INVISIBLE);
        }

        //정답을 확인한 이후에만 읽어주기 기능 동작
        ibQuizImage.setOnClickListener(view -> {
            if (flag) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = new MediaPlayer();
                }
                playVoice(mediaPlayer, name);
            }
        });

        ibleftbtn.setOnClickListener(view -> {
            --index;
            flag = false;
            tvQuizText.setVisibility(View.INVISIBLE);
            fragmentTransaction
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.container, quizFragment, "quizSub")
                    .commit();
        });

        tvAnswer.setOnClickListener(view -> {
            if (tvQuizText.getVisibility() == View.INVISIBLE) {
                tvQuizText.setVisibility(View.VISIBLE);
                tvAnswer.setText("정답 숨기기");
                flag = true;
            } else {
                tvQuizText.setVisibility(View.INVISIBLE);
                tvAnswer.setText("정답 보기");
                flag = false;
            }
        });

        ibrightbtn.setOnClickListener(view -> {
            ++index;
            flag = false;
            tvQuizText.setVisibility(View.INVISIBLE);
            fragmentTransaction
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.container, quizFragment, "quizSub")
                    .commit();
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
                    String tempname = "navercssflashcardquiz";
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
        String tempname = "navercssflashcardquiz";
        String Path_to_file = Environment.getExternalStorageDirectory() +
                File.separator + "NaverCSS/" + tempname + ".mp3";

        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
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

