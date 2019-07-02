package com.welfarerobotics.welfareapplication.streaming.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.welfarerobotics.welfareapplication.streaming.R
import com.welfarerobotics.welfareapplication.streaming.base.BaseActivity
import com.welfarerobotics.welfareapplication.streaming.databinding.SignInViewBinding
import com.welfarerobotics.welfareapplication.streaming.viewmodel.SignInViewModel


/**
 * @author : Hyunwoong
 * @when : 2019-07-02 오후 9:50
 * @homepage : https://github.com/gusdnd852
 */
class SignInActivity : BaseActivity() {

    lateinit var view: SignInViewBinding
    lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = DataBindingUtil.setContentView(this, R.layout.sign_in_view)
        viewModel = factory.create(SignInViewModel::class.java)
    }
}