package com.welfarerobotics.welfareapplcation.core.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.Toast;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.YoutubeApi;
import com.welfarerobotics.welfareapplcation.core.contents.paintwith.PaintWithActivity;
import com.welfarerobotics.welfareapplcation.core.contents.tangram.TangramSelecActivity;
import com.welfarerobotics.welfareapplcation.core.fairytale.FairytaleActivity;
import com.welfarerobotics.welfareapplcation.core.settings.SettingActivity;
import com.welfarerobotics.welfareapplcation.core.youtube.YoutubeActivity;
import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

import java.io.IOException;

public class MenuActivity extends AppCompatActivity {
    private String youtubeUrl;
    private QuickAction quickActionArt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ActionItem aipaintwith = new ActionItem(1,R.drawable.menu_paintwith);
        ActionItem aitangram = new ActionItem(2,R.drawable.menu_tangram);

        quickActionArt = new QuickAction(this, QuickAction.HORIZONTAL);
        quickActionArt.addActionItem(aipaintwith);
        quickActionArt.addActionItem(aitangram);

        ImageButton ibbackbtn = findViewById(R.id.backbutton);
        ImageButton ibSettings = findViewById(R.id.settings);
        ImageButton ibplaylang = findViewById(R.id.playlang);
        ImageButton ibkidssong = findViewById(R.id.listensong);
        ImageButton ibfollowbao = findViewById(R.id.followbao);
        ImageButton iplayart = findViewById(R.id.playart);

        ibbackbtn.setOnClickListener(view -> onBackPressed());

        ibSettings.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
        });

        ibplaylang.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ConversationEdit.class);
            startActivity(intent);
        });

        iplayart.setOnClickListener(v->{
            quickActionArt.show(v);
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
            Intent intent = new Intent(getApplicationContext(), FairytaleActivity.class);
            startActivity(intent);
        });

        quickActionArt.setOnActionItemClickListener(item -> {
            if(item == aipaintwith){
                Intent intent = new Intent(MenuActivity.this, PaintWithActivity.class);
                startActivity(intent);
            } else if(item == aitangram){
                Intent intent = new Intent(MenuActivity.this, TangramSelecActivity.class);
                startActivity(intent);
            }
        });
    }
}
