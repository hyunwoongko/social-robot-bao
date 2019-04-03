package com.welfarerobotics.welfareapplcation.ui.initial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import com.welfarerobotics.welfareapplcation.ui.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.R;

public class TutorialActivity extends BaseActivity {
    private ImageView startimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_tutorial);
        startimage = findViewById(R.id.start_imageView);
        startimage.setOnClickListener(view ->{
            Intent intent = new Intent(this, BlActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
