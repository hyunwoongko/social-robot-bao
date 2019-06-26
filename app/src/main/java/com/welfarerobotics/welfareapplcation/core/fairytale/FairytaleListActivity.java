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


        Intent intent = new Intent(this, FairytaleActivity.class);
        try {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    intent.putExtra("item",position);
                    startActivity(intent);
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
