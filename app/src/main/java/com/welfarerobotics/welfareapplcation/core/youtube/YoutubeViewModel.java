package com.welfarerobotics.welfareapplcation.core.youtube;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.view.View;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.welfarerobotics.welfareapplcation.core.base.BaseViewModel;
import com.welfarerobotics.welfareapplcation.entity.ServerCache;
import com.welfarerobotics.welfareapplcation.util.LiveData;
import lombok.Getter;

/**
 * @Author : Hyunwoong
 * @When : 4/24/2019 1:34 AM
 * @Homepage : https://github.com/gusdnd852
 */
public @Getter class YoutubeViewModel extends BaseViewModel implements YouTubePlayer.OnInitializedListener {

    private LiveData<String> youtubeUrl = new LiveData<>();
    private LiveData<YouTubePlayerView> youTubePlayerView = new LiveData<>();
    private LiveData activityFinishEvent = new LiveData();
    private LiveData showToastEvent = new LiveData();

    public YoutubeViewModel(@NonNull Application application) {
        super(application);
    }

    public void onCreate() {
        youTubePlayerView.getValue().initialize(ServerCache.getInstance().getYoutubekey(), this);
    }

    @Override protected void onCleared() {
        super.onCleared();
        youTubePlayerView.setValue(null);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.loadVideo(youtubeUrl.getValue()); //유튜브 비디오 ID(URL의 "v=" 뒤에 표시됨)
            youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
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
        }

        @Override
        public void onVideoEnded() {
            activityFinishEvent.call();
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
        }
    };
}
