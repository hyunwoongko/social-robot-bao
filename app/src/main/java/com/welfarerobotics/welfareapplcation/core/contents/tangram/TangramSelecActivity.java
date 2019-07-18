package com.welfarerobotics.welfareapplcation.core.contents.tangram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.entity.cache.TangramStageCache;
import com.welfarerobotics.welfareapplcation.util.Pool;
import com.welfarerobotics.welfareapplcation.util.Sound;
import com.welfarerobotics.welfareapplcation.util.data_util.UrlConverter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class TangramSelecActivity extends BaseActivity {
    private GridView mListView;
    private TangramListAdater myAdaterr;
    private ArrayList<String> items = TangramStageCache.getInstance().getURL();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tamgram_list_activity);

        mListView = (GridView) findViewById(R.id.grid_list);
        ImageButton rotateBtn = findViewById(R.id.rotatebtn);
        ImageButton backBtn = findViewById(R.id.backbutton);
        myAdaterr = new TangramListAdater(this);
        //dataSetting();
        mListView.setAdapter(myAdaterr);

        Intent intent = new Intent(this, TangramActivity.class);

        for (int i = 0; i < items.size(); i++) {
            myAdaterr.addItem(items.get(i));


        }




        myAdaterr.notifyDataSetChanged();

        try {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    intent.putExtra("image", items.get(position));
                    intent.putExtra("Position",position);
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


//
//    private void Init(){
//        Intent intent = new Intent(this, TangramActivity.class);
//        items = TangramStageCache.getInstance().getImages();
//         myAdaterr.clear();
//        for (int i = 0; i < items.size(); i++) {
//            myAdaterr.addItem(items.get(i));
//
//
//        }
//
//
//        myAdaterr.notifyDataSetChanged();
//
//        try {
//            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    TangramListItem myItem = (TangramListItem) parent.getAdapter().getItem(position);
//                    Bitmap sendBitmap = myItem.getStage();
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    sendBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    byte[] byteArray = stream.toByteArray();
//                    intent.putExtra("image", byteArray);
//                    startActivity(intent);
//                }
//            });
//        } catch (Exception e) {
//            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
//
//        }
//
//    }
}
