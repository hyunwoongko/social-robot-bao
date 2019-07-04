package com.welfarerobotics.welfareapplication.streaming.util.binding;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import rx.Observable;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author : Hyunwoong
 * @When : 2018-09-27 오전 3:52
 * @Homepage : https://github.com/gusdnd852
 */
public class ViewData<T> extends MutableLiveData<T> {

    private final AtomicBoolean mPending = new AtomicBoolean(false);

    public ViewData() {
    }

    public ViewData(T initialData) {
        setValue(initialData);
    }

    public void observe(@NonNull LifecycleOwner owner, @NonNull final Observer observer) {
        super.observe(owner, t -> {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t);
            }
        });
    }

    public Observable observable(LifecycleOwner owner) {
        return Observable.create(emitter ->
                observe(owner, data -> {
                    try {
                        emitter.onNext(data);
                        emitter.onCompleted();
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                }));
    }

    public void setValue(@Nullable T t) {
        mPending.set(true);
        super.setValue(t);
    }

    @Override public void postValue(T value) {
        mPending.set(true);
        super.postValue(value);
    }

    @MainThread public void call() {
        setValue(null);
    }

    public void callBackground() {
        postValue(null);
    }
}