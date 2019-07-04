package com.welfarerobotics.welfareapplication.streaming.core.view;

import com.welfarerobotics.welfareapplication.streaming.R;
import com.welfarerobotics.welfareapplication.streaming.base.BaseActivity;
import com.welfarerobotics.welfareapplication.streaming.core.viewmodel.SignInViewModel;
import com.welfarerobotics.welfareapplication.streaming.databinding.SignInView;
import com.welfarerobotics.welfareapplication.streaming.di.DaggerSignInFactory;
import com.welfarerobotics.welfareapplication.streaming.di.SignInFactory;
import com.welfarerobotics.welfareapplication.streaming.di.SignInFactory.SignInModule;


/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 1:11
 * @homepage : https://github.com/gusdnd852
 */
public class SignInActivity extends BaseActivity<SignInView, SignInViewModel> {

    @Override protected int injectView() {
        DaggerSignInFactory.builder()
                .signInModule(new SignInModule())
                .build().inject(this);

        return R.layout.sign_in_view;
    }

    @Override protected void observe() {

    }
}
