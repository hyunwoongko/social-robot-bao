package com.welfarerobotics.welfareapplication.streaming.util.etc;

import lombok.Getter;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 1:44
 * @homepage : https://github.com/gusdnd852
 * <p>
 * Object Pool Pattern 으로 구현
 */

@SuppressWarnings("unchecked")
public class Pool {
    private final static ConcurrentHashMap<String, Object> objectPool = new ConcurrentHashMap<>();
    static ExecutorService thread = Executors.newCachedThreadPool();
    public final static Scheduler background = Schedulers.from(thread);
    public final static Scheduler computation = Schedulers.computation();
    public final static Scheduler io = Schedulers.io();
    public final static Scheduler main = AndroidSchedulers.mainThread();

    public static void addObject(String key, Object val) {
        objectPool.put(key, val);
    }

    public static <T> T getObject(String key, Class<T>... clazz) {
        return (T) objectPool.get(key);
    }

    public static void removeObject(String key){
        objectPool.remove(key);
    }
}
