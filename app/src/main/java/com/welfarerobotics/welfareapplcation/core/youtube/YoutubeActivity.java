package com.welfarerobotics.welfareapplcation.core.youtube;

import android.widget.Toast;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.TransparentActivity;
import com.welfarerobotics.welfareapplcation.databinding.YoutubeView;

public class YoutubeActivity extends TransparentActivity<YoutubeView, YoutubeViewModel> {

    @Override public void onCreateView(int layout, Class clazz) {
        super.onCreateView(R.layout.youtube_view, YoutubeViewModel.class);
    }

    @Override public void observeData() {
        viewModel.getYoutubeUrl().setValue(getIntent().getExtras().getString("url"));
        viewModel.getYouTubePlayerView().setValue(view.YoutubeView);
    }

    @Override public void observeAction() {
        viewModel.getActivityFinishEvent().observe(this, o -> this.finish());
        viewModel.getShowToastEvent().observe(this, o -> {
            Toast.makeText(getApplication(), "화면을 누르면 재생을 종료합니다", Toast.LENGTH_SHORT).show();
        });
    }
}
