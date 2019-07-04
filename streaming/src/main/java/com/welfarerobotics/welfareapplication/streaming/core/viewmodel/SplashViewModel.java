package com.welfarerobotics.welfareapplication.streaming.core.viewmodel;

import android.arch.lifecycle.ViewModel;
import com.welfarerobotics.welfareapplication.streaming.util.binding.ViewData;
import com.welfarerobotics.welfareapplication.streaming.util.binding.Event;
import com.welfarerobotics.welfareapplication.streaming.util.etc.Pool;
import lombok.Getter;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 12:36
 * @homepage : https://github.com/gusdnd852
 */

@Getter
public class SplashViewModel extends ViewModel {

    private ViewData playSound = new ViewData();
    private ViewData moveActivity = new ViewData();

    private Event splashEvent = v -> Observable.just(v)
            .doOnNext(view -> playSound.call())
            .delay(3, TimeUnit.SECONDS)
            .observeOn(Pool.main)
            .doOnNext(view -> moveActivity.call())
            .subscribe();
}
