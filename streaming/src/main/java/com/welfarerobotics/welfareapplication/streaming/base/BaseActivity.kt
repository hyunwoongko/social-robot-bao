package com.welfarerobotics.welfareapplication.streaming.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.welfarerobotics.welfareapplication.streaming.factory.ViewModelFactory


/**
 * @author : Hyunwoong
 * @when : 2019-07-02 오후 10:47
 * @homepage : https://github.com/gusdnd852
 */
abstract class BaseActivity : AppCompatActivity() {

    lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = ViewModelFactory(application)
    }
}