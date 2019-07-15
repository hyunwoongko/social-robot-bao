package com.welfarerobotics.welfareapplcation.core.fairytale;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.contents.tangram.TangramActivity;
import com.welfarerobotics.welfareapplcation.core.youtube.YoutubeActivity;
import com.welfarerobotics.welfareapplcation.entity.FairyTail;

import java.util.ArrayList;

public class FairytaleListActivity extends BaseActivity {


    private GridView mListView;
    private FairytaleListAdapter myAdaterr;
    private ArrayList<FairyTail> items = FairytailCache.getInstance().getFairytail();
    private ImageButton rotateBtn;
    private ImageButton backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fairytail_list_activity);

        mListView = (GridView) findViewById(R.id.grid_list);
        rotateBtn = findViewById(R.id.rotatebtn);
        backBtn = findViewById(R.id.backbutton);
        myAdaterr = new FairytaleListAdapter(this);
        mListView.setAdapter(myAdaterr);

        for (int i = 0; i < items.size(); i++) {
            myAdaterr.addItem(items.get(i));


        }


        Intent intent = new Intent(this, YoutubeActivity.class);
        try {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String url = "url";

                    switch (position) {
                        case 0: // 개미와 배짱이
                            intent.putExtra(url, "JyqquUYtvN0");
                            startActivity(intent);
                            break;
                        case 1: // 아돼삼
                            intent.putExtra(url, "9rYvtjmem08");
                            startActivity(intent);
                            break;
                        case 2: // 미녀와 야수
                            intent.putExtra(url, "n7pk3yIKUQk");
                            startActivity(intent);
                            break;
                        case 3: // 파랑새
                            intent.putExtra(url, "_f2AE5vSLFM");
                            startActivity(intent);
                            break;
                        case 4: // 성냥
                            intent.putExtra(url, "AA7e2xMSrow");
                            startActivity(intent);
                            break;
                        case 5: // 인어공주
                            intent.putExtra(url, "Rtu3kTAE06Y");
                            startActivity(intent);
                            break;
                        case 6: // 오리
                            intent.putExtra(url, "RqZV70hwozQ");
                            startActivity(intent);
                            break;
                        case 7: // 흥부놀부
                            intent.putExtra(url, "vEbja_KLUYU");
                            startActivity(intent);
                            break;
                        case 8: // 잭콩
                            intent.putExtra(url, "bLzFnELQeFQ");
                            startActivity(intent);
                            break;
                        case 9: // 콩팥
                            intent.putExtra(url, "c1n0G1qnBfk");
                            startActivity(intent);
                            break;
                        case 10: // 피노키오
                            intent.putExtra(url, "qJy9j7eLWvs");
                            startActivity(intent);
                            break;
                        case 11: // 공주 개구리
                            intent.putExtra(url, "0KybTTk1rQY");
                            startActivity(intent);
                            break;
                        case 12: // 피터팬
                            intent.putExtra(url, "eWwAnBQvV2Q");
                            startActivity(intent);
                            break;
                        case 13: // 별주부전
                            intent.putExtra(url, "joaffXD6L7Y");
                            startActivity(intent);
                            break;
                        case 14: // 빨모
                            intent.putExtra(url, "On0nD9H2anY");
                            startActivity(intent);
                            break;
                        case 15: // 눈의여왕
                            intent.putExtra(url, "2JFoGsQ5n0w");
                            startActivity(intent);
                            break;
                        case 16: // 선녀와 나무꾼
                            intent.putExtra(url, "XUbbdLIbj-4");
                            startActivity(intent);
                            break;
                        case 17: // 장신고
                            intent.putExtra(url, "E6OUVuAL69c");
                            startActivity(intent);
                            break;
                        case 18: // 임금귀는 당나귀
                            intent.putExtra(url, "10DD8zCV4tA");
                            startActivity(intent);
                            break;
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();

        }
        backBtn.setClickable(true);
        backBtn.setOnClickListener(view -> onBackPressed());

        rotateBtn.setClickable(true);
        rotateBtn.setOnClickListener(view -> {
            myAdaterr.clear();
            for (int i = 0; i < items.size(); i++) {
                myAdaterr.addItem(items.get(i));


            }
            myAdaterr.notifyDataSetChanged();

        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        myAdaterr.notifyDataSetChanged();

    }
}
