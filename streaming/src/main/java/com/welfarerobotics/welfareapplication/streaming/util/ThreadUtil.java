package com.welfarerobotics.welfareapplication.streaming.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 1:50
 * @homepage : https://github.com/gusdnd852
 */
public class ThreadUtil {

    public static <T> T async(Callable<T> callable) {
        Future<T> future = Pool.thread.submit(callable);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void async(Runnable runnable) {
        Pool.thread.execute(runnable);
    }
}
