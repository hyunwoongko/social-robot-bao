package com.welfarerobotics.welfareapplication.streaming.view;

import android.databinding.DataBindingUtil;
import com.welfarerobotics.welfareapplication.streaming.R;
import com.welfarerobotics.welfareapplication.streaming.base.BaseActivity;
import com.welfarerobotics.welfareapplication.streaming.databinding.SignInView;
import com.welfarerobotics.welfareapplication.streaming.viewmodel.SignInViewModel;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 1:11
 * @homepage : https://github.com/gusdnd852
 */
public class SignInActivity extends BaseActivity<SignInView, SignInViewModel> {

    @Override protected int bindView() {
        return R.layout.sign_in_view;
    }

    @Override protected void observe() {

    }
}
