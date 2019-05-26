package com.welfarerobotics.welfareapplcation.core.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.Toast;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.YoutubeApi;
import com.welfarerobotics.welfareapplcation.core.contents.dictation.DicationActivity;
import com.welfarerobotics.welfareapplcation.core.contents.emotioncard.EmotioncardActivity;
import com.welfarerobotics.welfareapplcation.core.contents.flashcard.FlashcardActivity;
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
    private QuickAction quickActionCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //미술놀이 퀵액션 생성 (함께그려요, 탱그램)
        ActionItem aipaintwith = new ActionItem(1,R.drawable.menu_paintwith);
        ActionItem aitangram = new ActionItem(2,R.drawable.menu_tangram);

        quickActionArt = new QuickAction(this, QuickAction.HORIZONTAL);
        quickActionArt.addActionItem(aipaintwith);
        quickActionArt.addActionItem(aitangram);

        //카드놀이 퀵액션 생성(단어카드, 감정카드)
        ActionItem aiflashcard = new ActionItem(1,R.drawable.menu_flashcard);
        ActionItem aiemotioncard = new ActionItem(2,R.drawable.menu_emotioncard);

        quickActionCard = new QuickAction(this, QuickAction.HORIZONTAL);
        quickActionCard.addActionItem(aiflashcard);
        quickActionCard.addActionItem(aiemotioncard);

        ImageButton ibbackbtn = findViewById(R.id.backbutton);
        ImageButton ibSettings = findViewById(R.id.settings);
        ImageButton ibplaylang = findViewById(R.id.playlang);
        ImageButton ibkidssong = findViewById(R.id.listensong);
        ImageButton ibfollowbao = findViewById(R.id.followbao);
        ImageButton iplayart = findViewById(R.id.playart);
        ImageButton ibplaycard = findViewById(R.id.playcard);

        ibbackbtn.setOnClickListener(view -> onBackPressed());

        ibSettings.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
        });

        ibplaylang.setOnClickListener(view -> {
//            Intent intent = new Intent(getApplicationContext(), ConversationEdit.class);
            Intent intent = new Intent(getApplicationContext(), DicationActivity.class);
            startActivity(intent);
        });

        iplayart.setOnClickListener(v-> quickActionArt.show(v));

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

        ibplaycard.setOnClickListener(v -> quickActionCard.show(v));

        quickActionArt.setOnActionItemClickListener(item -> {
            if(item == aipaintwith){
                Intent intent = new Intent(MenuActivity.this, PaintWithActivity.class);
                startActivity(intent);
            } else if(item == aitangram){
                Intent intent = new Intent(MenuActivity.this, TangramSelecActivity.class);
                startActivity(intent);
            }
        });

        quickActionCard.setOnActionItemClickListener(item -> {
            if(item == aiflashcard){
                Intent intent = new Intent(MenuActivity.this, FlashcardActivity.class);
                startActivity(intent);
            } else if(item == aiemotioncard){
                Intent intent = new Intent(MenuActivity.this, EmotioncardActivity.class);
                startActivity(intent);
            }
        });
    }
}
