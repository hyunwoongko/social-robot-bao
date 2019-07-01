package com.welfarerobotics.welfareapplcation.core.menu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ImageButton;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.YoutubeApi;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.contents.common_sense.CommonQuizActivity;
import com.welfarerobotics.welfareapplcation.core.contents.dictation.DictationActivity;
import com.welfarerobotics.welfareapplcation.core.contents.emotioncard.EmotioncardActivity;
import com.welfarerobotics.welfareapplcation.core.contents.flashcard.FlashcardActivity;
import com.welfarerobotics.welfareapplcation.core.contents.paintwith.PaintWithActivity;
import com.welfarerobotics.welfareapplcation.core.contents.tangram.TangramSelecActivity;
import com.welfarerobotics.welfareapplcation.core.fairytale.FairytaleActivity;
import com.welfarerobotics.welfareapplcation.core.fairytale.FairytaleListActivity;
import com.welfarerobotics.welfareapplcation.core.settings.SettingActivity;
import com.welfarerobotics.welfareapplcation.core.youtube.YoutubeActivity;
import com.welfarerobotics.welfareapplcation.util.Pool;
import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MenuActivity extends BaseActivity {
    private String youtubeUrl;
    private QuickAction quickActionArt;
    private QuickAction quickActionLang;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //미술놀이 퀵액션 생성 (함께그려요, 탱그램)
        ActionItem aipaintwith = new ActionItem(1, R.drawable.menu_paintwith);
        ActionItem aitangram = new ActionItem(2, R.drawable.menu_tangram);

        quickActionArt = new QuickAction(this, QuickAction.HORIZONTAL);
        quickActionArt.addActionItem(aipaintwith);
        quickActionArt.addActionItem(aitangram);

        //언어놀이 퀵액션 생성(받아쓰기, 낱말카드)
        ActionItem aidictation = new ActionItem(1, R.drawable.menu_dictation);
        ActionItem aiflashcard = new ActionItem(2, R.drawable.menu_flashcard);

        quickActionLang = new QuickAction(this, QuickAction.HORIZONTAL);
        quickActionLang.addActionItem(aidictation);
        quickActionLang.addActionItem(aiflashcard);

        ImageButton ibbackbtn = findViewById(R.id.backbutton);
        ImageButton ibSettings = findViewById(R.id.settings);
        ImageButton ibplaylang = findViewById(R.id.playlang);
        ImageButton ibkidssong = findViewById(R.id.listensong);
        ImageButton ibfollowbao = findViewById(R.id.followbao);
        ImageButton ibplayart = findViewById(R.id.playart);
        ImageButton ibemotioncard = findViewById(R.id.emotioncard);
        ImageButton ibplayfairytale = findViewById(R.id.playfairytale);
        ImageButton ibQuiz = findViewById(R.id.commonsense);

        ibbackbtn.setOnClickListener(view -> onBackPressed());

        ibSettings.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
        });

        ibQuiz.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), CommonQuizActivity.class));
        });

        ibplaylang.setOnClickListener(view -> quickActionLang.show(view));

        ibplayart.setOnClickListener(view -> quickActionArt.show(view));

        ibkidssong.setOnClickListener(view -> {

            Future<String> futureYoutube = Pool.youtubeThread.submit(() -> {
                try {
                    return YoutubeApi.getYoutube("동요");
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            });
            //Youtube URL에서 ID만 추출
            try {
                youtubeUrl = futureYoutube.get(); // 쓰레드 처리 결과를 수신
                int UrlIdIndex = youtubeUrl.indexOf("=");
                youtubeUrl = youtubeUrl.substring(UrlIdIndex + 1);
                //YoutubeActivity 실행 및 URL 전달
                Intent youtubeIntent = new Intent(getApplicationContext(), YoutubeActivity.class);
                youtubeIntent.putExtra("url", youtubeUrl);
                startActivity(youtubeIntent);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        ibfollowbao.setOnClickListener(view -> {
            // new Dance();
            //TODO : 여기 잠시 주석 처리 해놓음 !!
        });

        ibemotioncard.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EmotioncardActivity.class);
            startActivity(intent);
        });

        ibplayfairytale.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), FairytaleListActivity.class);
            startActivity(intent);
        });

        quickActionArt.setOnActionItemClickListener(item -> {
            if (item == aipaintwith) {
                Intent intent = new Intent(MenuActivity.this, PaintWithActivity.class);
                startActivity(intent);
            } else if (item == aitangram) {
                Intent intent = new Intent(MenuActivity.this, TangramSelecActivity.class);
                startActivity(intent);
            }
        });

        quickActionLang.setOnActionItemClickListener(item -> {
            if (item == aidictation) {
                Intent intent = new Intent(MenuActivity.this, DictationActivity.class);
                startActivity(intent);
            } else if (item == aiflashcard) {
                Intent intent = new Intent(MenuActivity.this, FlashcardActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String name = bundle.getString("key");
            if (name != null && name.equals("FairytaleScenario")) {
                startActivity(new Intent(getApplicationContext(), FairytaleActivity.class));
            }
        }
    }
}
