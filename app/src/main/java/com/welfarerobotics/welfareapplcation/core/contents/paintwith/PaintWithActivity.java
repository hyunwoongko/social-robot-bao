package com.welfarerobotics.welfareapplcation.core.contents.paintwith;

import android.content.Intent;
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
        setContentView(R.layout.activity_paint_with);
        canvasView = findViewById(R.id.canvas_view);

        findViewById(R.id.redo_button).setOnClickListener(v -> {
            canvasView.redo();
        });

        findViewById(R.id.undo_button).setOnClickListener(v -> {
            canvasView.undo();
        });

        findViewById(R.id.clear_button).setOnClickListener(v -> {
            canvasView.clear();
        });

        findViewById(R.id.pencil_button).setOnClickListener(v -> {
            this.canvasView.setMode(CanvasView.Mode.DRAW);    // for drawing
        });

        findViewById(R.id.eraser_button).setOnClickListener(v -> {
            this.canvasView.setMode(CanvasView.Mode.ERASER);  // for using Eraser
        });

        findViewById(R.id.galley_button).setOnClickListener(v -> {
            startActivity(new Intent(PaintWithActivity.this, GalleryActivity.class));
        });
    }
}
