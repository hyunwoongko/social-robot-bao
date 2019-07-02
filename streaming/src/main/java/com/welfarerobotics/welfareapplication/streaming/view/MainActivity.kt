package com.welfarerobotics.welfareapplication.streaming.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.welfarerobotics.welfareapplication.streaming.R
import com.welfarerobotics.welfareapplication.streaming.base.BaseActivity
import com.welfarerobotics.welfareapplication.streaming.databinding.MainViewBinding
import com.welfarerobotics.welfareapplication.streaming.viewmodel.MainViewModel

/**
 * @author : Hyunwoong
 * @when : 2019-07-02 오후 9:50
 * @homepage : https://github.com/gusdnd852
 */
class MainActivity : BaseActivity() {

    lateinit var view: MainViewBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = DataBindingUtil.setContentView(this, R.layout.main_view)
        viewModel = factory.create(MainViewModel::class.java)

    }
}
