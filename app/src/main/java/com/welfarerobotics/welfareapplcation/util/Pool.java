package com.welfarerobotics.welfareapplcation.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 2:55 PM
 * @homepage : https://github.com/gusdnd852
 */
public class Pool {
    public static ExecutorService threadPool = Executors.newCachedThreadPool();
}
