package com.welfarerobotics.welfareapplcation.core.base;

import android.arch.lifecycle.*;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.welfarerobotics.welfareapplcation.BR;

/**
 * @Author : Hyunwoong
 * @When : 4/24/2019 12:33 AM
 * @Homepage : https://github.com/gusdnd852
 *
 * 투명한 액티비티의 parent 클래스
 */
public abstract class TransparentActivity<V extends ViewDataBinding, VM extends AndroidViewModel>
        extends YouTubeBaseActivity implements BaseLifeCycle, LifecycleOwner {

    protected V view;
    protected VM viewModel;
    private int layout;
    private Class<VM> clazz;

    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    // 라이프사이클 오너

    @NonNull @Override public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    @Override protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        onCreateView(layout, clazz);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.0f; //메인액티비티 투명도 조절
        layoutParams.alpha = 0.0f;
        getWindow().setAttributes(layoutParams);

        Display dp = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (dp.getWidth() * 1.0);
        int height = (int) (dp.getHeight() * 1.0);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        addListener();
        observeData();
        observeAction();
    }

    @Override public void onCreateView(int layout, Class<? extends AndroidViewModel> clazz) {
        this.layout = layout;
        this.clazz = (Class<VM>) clazz;

        view = DataBindingUtil.setContentView(this, layout);
        viewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())
                .create((Class<VM>) clazz);
        view.setVariable(BR.viewModel, viewModel);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                finish();
                break;
            case MotionEvent.ACTION_UP:    //화면을 터치했다 땠을때
                break;
            case MotionEvent.ACTION_MOVE:    //화면을 터치하고 이동할때
                break;
        }
        return super.onTouchEvent(event);
    }
}
