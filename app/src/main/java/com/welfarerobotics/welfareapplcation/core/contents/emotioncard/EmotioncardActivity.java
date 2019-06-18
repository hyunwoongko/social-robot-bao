package com.welfarerobotics.welfareapplcation.core.contents.emotioncard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;
import android.widget.ImageButton;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.VoiceActivity;
import com.welfarerobotics.welfareapplcation.util.Sound;

public class EmotioncardActivity extends VoiceActivity {
    private EmotioncardFragment emotioncardFragment = null;
    public static int emotionIndex = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_emotioncard);

        emotioncardFragment = new EmotioncardFragment();

        ImageButton ibBackbutton = findViewById(R.id.backButton);

        emotionIndex = 1;

        ibBackbutton.setOnClickListener(view -> onBackPressed());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .setCustomAnimations(R.anim.activity_fade_in, R.anim.activity_fade_out)
                .replace(R.id.container, emotioncardFragment)
                .commit();
    }
    @Override protected void onResume() {
        super.onResume();
        Sound.get().resume(this, R.raw.emotion_card).loop(true);
    }

    @Override protected void onPause() {
        super.onPause();
        Sound.get().pause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        Sound.get().stop();
    }
}
