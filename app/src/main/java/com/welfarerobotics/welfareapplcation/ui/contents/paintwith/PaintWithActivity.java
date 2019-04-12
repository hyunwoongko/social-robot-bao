package com.welfarerobotics.welfareapplcation.ui.contents.paintwith;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.welfarerobotics.welfareapplcation.R;

/**
 * @Author : Hyunwoong
 * @When : 4/9/2019 1:51 AM
 * @Homepage : https://github.com/gusdnd852
 */
public class PaintWithActivity extends AppCompatActivity {

    private CanvasView canvasView;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paintwith);
        canvasView = findViewById(R.id.paint_with_canvas);


    }
}
