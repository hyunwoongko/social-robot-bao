package com.welfarerobotics.welfareapplcation.core.base;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;
import com.welfarerobotics.welfareapplcation.BR;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.util.ToastType;
import es.dmoral.toasty.Toasty;

public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel>
        extends AppCompatActivity implements BaseLifeCycle {

    private int layout;
    private Class<VM> clazz;
    protected V view;
    protected VM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onCreateView(layout, clazz);
        addListener();
        observeData();
        observeAction();
        viewModel.onCreate();
    }

    @Override public void onCreateView(int layout, Class<? extends AndroidViewModel> clazz) {
        this.layout = layout;
        this.clazz = (Class<VM>) clazz;

        view = DataBindingUtil.setContentView(this, layout);
        viewModel = ViewModelProviders.of(this).get((Class<VM>) clazz);
        view.setVariable(BR.viewModel, viewModel);
    }

    //액션바 터치이벤트
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int X = (int) event.getX();
        int Y = (int) event.getY();
        int eventaction = event.getAction();
        if (Y < 400) onWindowFocusChanged(true);
        return true;
    }

    //시스템 다이얼로그 닫기 로직 추가
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    @Override public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    @Override public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    public void showToast(String msg, ToastType type) {
        if (type == ToastType.error) {
            Toasty.error(this, msg, Toast.LENGTH_SHORT).show();
        } else if (type == ToastType.normal) {
            Toasty.normal(this, msg, Toast.LENGTH_SHORT).show();
        } else if (type == ToastType.info) {
            Toasty.info(this, msg, Toast.LENGTH_SHORT).show();
        } else if (type == ToastType.success) {
            Toasty.success(this, msg, Toast.LENGTH_SHORT).show();
        } else if (type == ToastType.warning) {
            Toasty.warning(this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
