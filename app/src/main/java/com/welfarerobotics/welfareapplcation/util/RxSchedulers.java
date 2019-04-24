package com.welfarerobotics.welfareapplcation.util;

/**
 * @Author : Hyunwoong
 * @When : 4/23/2019 11:02 PM
 * @Homepage : https://github.com/gusdnd852
 */

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author : Hyunwoong
 * @When : 2018-09-27 오후 4:17
 * @Homepage : https://github.com/gusdnd852
 */
public class RxSchedulers {

    private static Executor backgroundExecutor = Executors.newCachedThreadPool();
    private static Executor internetExecutor = Executors.newCachedThreadPool();
    private static Scheduler BACKGROUND_SCHEDULERS = Schedulers.from(backgroundExecutor);
    private static Scheduler INTERNET_SCHEDULERS = Schedulers.from(internetExecutor);

    public static Scheduler backgroundThread() {
        return BACKGROUND_SCHEDULERS;
    }

    public static Scheduler ioThread() {
        return Schedulers.io();
    }

    public static Scheduler computeThread() {
        return Schedulers.computation();
    }

    public static Scheduler androidThread() {
        return AndroidSchedulers.mainThread();
    }

    public static Scheduler networkThread() {
        return INTERNET_SCHEDULERS;
    }
}