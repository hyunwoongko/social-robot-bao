package com.welfarerobotics.welfareapplication.streaming.base;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.android.databinding.library.baseAdapters.BR;
import com.welfarerobotics.welfareapplication.streaming.base.extension.UIExtension;
import lombok.Getter;

import javax.inject.Inject;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 12:21
 * @homepage : https://github.com/gusdnd852
 * <p>
 * Template Method 패턴으로 구현하기
 */
@SuppressWarnings("unchecked")
public abstract class BaseActivity<V extends ViewDataBinding,
        VM extends ViewModel> extends UIExtension {

    @Inject
    protected VM viewModel;
    protected V view;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = DataBindingUtil.setContentView(this, injectView());
        view.setVariable(BR.viewModel, viewModel);
        this.observe();
    }

    protected abstract int injectView();

    protected abstract void observe();
}
