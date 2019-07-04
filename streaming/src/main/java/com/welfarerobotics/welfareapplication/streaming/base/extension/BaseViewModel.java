package com.welfarerobotics.welfareapplication.streaming.base.extension;

import android.arch.lifecycle.ViewModel;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author : Hyunwoong
 * @when : 2019-07-04 오후 4:45
 * @homepage : https://github.com/gusdnd852
 */
public abstract class BaseViewModel extends ViewModel {

    private CompositeSubscription subscription = new CompositeSubscription();

    protected void addSubscription(Subscription subscription) {
        this.subscription.add(subscription);
    }

    @Override protected void onCleared() {
        super.onCleared();
        subscription.unsubscribe();
    }
}
