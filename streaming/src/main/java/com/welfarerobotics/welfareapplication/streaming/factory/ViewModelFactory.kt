package com.welfarerobotics.welfareapplication.streaming.factory

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider


/**
 * @author : Hyunwoong
 * @when : 2019-07-02 오후 10:41
 * @homepage : https://github.com/gusdnd852
 */

class ViewModelFactory(application: Application) :
        ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return super.create(modelClass)
    }
}