package com.welfarerobotics.welfareapplication.streaming.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.welfarerobotics.welfareapplication.streaming.util.LiveData
import java.util.*


/**
 * @author : Hyunwoong
 * @when : 2019-07-02 오후 9:49
 * @homepage : https://github.com/gusdnd852
 */
class SplashViewModel(application: Application) : AndroidViewModel(application) {

    val splashEvent: LiveData<Any> = LiveData()

    fun splashEvent() {

    }
}