package com.welfarerobotics.welfareapplcation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.welfarerobotics.welfareapplcation.chat_api.ApiServer;

public class YoutubeActivity extends Activity
        implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlayerStateChangeListener {

    private ImageButton imageButton;
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView playerView;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams  layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags  = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount  = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_youtube);

        Display dp = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int)(dp.getWidth()*0.7);
        int height = (int)(dp.getHeight()*0.8);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        Intent youtubeIntent = getIntent();
        url = youtubeIntent.getStringExtra("url");
        int index = url.indexOf("=");
        url = url.substring(index + 1);

        System.out.println(url);
        imageButton = findViewById(R.id.MusicStopButton);
        playerView = findViewById(R.id.YoutubeView);

        playerView.initialize(ApiServer.youtubeKey, this);

        imageButton.setOnClickListener(view -> {
            if (youTubePlayer.isPlaying()) {
                youTubePlayer.release();
            }
            finish();
        });
    }

    //YouTubePlayer.OnInitializedListener
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.loadVideo(url); //유튜브 비디오 ID(URL의 "v=" 뒤에 표시됨)

        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


    //YouTubePlayer.PlayerStateChangeListener
    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {
        Toast.makeText(getApplicationContext(),"음표를 누르면 재생을 종료합니다", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVideoEnded() {
        if (youTubePlayer.isPlaying()) {
            youTubePlayer.release();
        }
        finish();
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }
}
