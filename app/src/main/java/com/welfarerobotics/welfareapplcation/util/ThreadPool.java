package com.welfarerobotics.welfareapplcation.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author : Hyunwoong
 * @When : 3/23/2019 10:53 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class ThreadPool {
    public static ExecutorService executor = Executors.newCachedThreadPool();
}
