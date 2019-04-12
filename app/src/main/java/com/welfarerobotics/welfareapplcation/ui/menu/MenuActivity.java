package com.welfarerobotics.welfareapplcation.ui.menu;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.Toast;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.api.chat.chatutil.Fairytale;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.YoutubeApi;
import com.welfarerobotics.welfareapplcation.ui.settings.SettingActivity;
import com.welfarerobotics.welfareapplcation.ui.youtube.YoutubeActivity;
import com.welfarerobotics.welfareapplcation.util.OnSwipeTouchListener;

import java.io.IOException;

public class MenuActivity extends AppCompatActivity {
    private String youtubeUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageButton ibbackbtn = findViewById(R.id.backbutton);
        ImageButton ibSettings = findViewById(R.id.settings);
        ImageButton ibplaylang = findViewById(R.id.playlang);
        ImageButton ibkidssong = findViewById(R.id.listensong);
        ImageButton ibfollowbao = findViewById(R.id.followbao);

        ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        ibbackbtn.setOnClickListener(view -> onBackPressed());
        ibSettings.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
        });
        ibplaylang.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ConversationEdit.class);
            startActivity(intent);
        });
        ibkidssong.setOnClickListener(view -> {
            try {
                youtubeUrl = YoutubeApi.getYoutube("동요");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Youtube URL에서 ID만 추출
            int UrlIdIndex = youtubeUrl.indexOf("=");
            youtubeUrl = youtubeUrl.substring(UrlIdIndex + 1);
            Toast.makeText(this, "화면을 누르면 재생이 종료됩니다.", Toast.LENGTH_SHORT).show();
            //YoutubeActivity 실행 및 URL 전달
            Intent youtubeIntent = new Intent(getApplicationContext(), YoutubeActivity.class);
            youtubeIntent.putExtra("url", youtubeUrl);
            startActivity(youtubeIntent);
        });
        ibfollowbao.setOnClickListener(view -> {
            Fairytale.get().play();
        });
    }
}
