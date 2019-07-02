package com.welfarerobotics.welfareapplication.streaming.view


import android.databinding.DataBindingUtil
import android.os.Bundle
import com.welfarerobotics.welfareapplication.streaming.R
import com.welfarerobotics.welfareapplication.streaming.base.BaseActivity
import com.welfarerobotics.welfareapplication.streaming.databinding.SplashViewBinding
import com.welfarerobotics.welfareapplication.streaming.viewmodel.SplashViewModel

/**
 * @author : Hyunwoong
 * @when : 2019-07-02 오후 9:50
 * @homepage : https://github.com/gusdnd852
 */
class SplashActivity : BaseActivity() {

    lateinit var view : SplashViewBinding
    lateinit var viewModel : SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = DataBindingUtil.setContentView(this, R.layout.splash_view)
        viewModel = factory.create(SplashViewModel::class.java)

    }
}