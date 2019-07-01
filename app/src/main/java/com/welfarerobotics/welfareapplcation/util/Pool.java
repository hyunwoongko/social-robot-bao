package com.welfarerobotics.welfareapplcation.util;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 2:55 PM
 * @homepage : https://github.com/gusdnd852
 */
public class Pool {
    public static ExecutorService mouthThread = Executors.newCachedThreadPool();
    public static ExecutorService hormoneThread = Executors.newCachedThreadPool();
    public static ExecutorService eyeThread = Executors.newSingleThreadExecutor();
    public static ExecutorService youtubeThread = Executors.newSingleThreadExecutor();
    public static ExecutorService sttThread = Executors.newSingleThreadExecutor();
}
