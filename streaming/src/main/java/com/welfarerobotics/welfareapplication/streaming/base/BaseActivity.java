package com.welfarerobotics.welfareapplication.streaming.base;

import android.arch.lifecycle.ViewModel;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.welfarerobotics.welfareapplication.streaming.BR;
import com.welfarerobotics.welfareapplication.streaming.base.extend.UtilExtends;
import com.welfarerobotics.welfareapplication.streaming.factory.ViewModelFactory;

import java.lang.ref.WeakReference;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 12:21
 * @homepage : https://github.com/gusdnd852
 * <p>
 * Template Method 패턴으로 구현하기
 */
@SuppressWarnings("unchecked")
public abstract class BaseActivity<V extends ViewDataBinding, VM extends ViewModel>
        extends UtilExtends {

    protected WeakReference<V> view;
    protected VM viewModel;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelFactory factory = new ViewModelFactory(getApplication());
        Object vm = factory.create(viewModel.getClass());
        viewModel = (VM) vm;
        view = new WeakReference<>(DataBindingUtil.setContentView(this, bindView()));
        view.get().setVariable(BR.viewModel, viewModel);
        this.observe();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        view.get().unbind();
        viewModel = null;
        view = null;
    }

    protected abstract int bindView();
    protected abstract void observe();
}
