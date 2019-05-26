package com.welfarerobotics.welfareapplcation.core.contents.flashcard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;
import android.widget.ImageButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;

public class FlashcardActivity extends BaseActivity {
    private ModeFragment modeFragment = null;
    public static int index = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_flashcard);

        modeFragment = new ModeFragment();

        ImageButton ibBackbutton = findViewById(R.id.backButton);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .defaultDisplayImageOptions(options)
                .memoryCacheSize(4*512*512)
                .diskCacheSize(100*1024*1024)
                .diskCacheFileCount(100)
                .build();

        ImageLoader.getInstance().init(config);

        ibBackbutton.setOnClickListener(view -> {
            Fragment mode = getSupportFragmentManager().findFragmentByTag("mode");

            if (mode != null && mode.isVisible()) {
                onBackPressed();
            } else{
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction
                        .setCustomAnimations(R.anim.activity_fade_in, R.anim.activity_fade_out)
                        .replace(R.id.container, modeFragment, "mode")
                        .commit();
            }
        });

        //실행시 처음으로 표시되는 프래그먼트는 모드 프래그먼트
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .setCustomAnimations(R.anim.activity_fade_in, R.anim.activity_fade_out)
                .replace(R.id.container, modeFragment, "mode")
                .commit();
    }
}
