package com.welfarerobotics.welfareapplcation.core.fairytale;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.view.View;
import com.welfarerobotics.welfareapplcation.api.chat.chatutil.Fairytale;
import com.welfarerobotics.welfareapplcation.core.base.BaseViewModel;
import com.welfarerobotics.welfareapplcation.util.RxSchedulers;
import rx.Observable;

/**
 * @Author : Hyunwoong
 * @When : 4/24/2019 2:02 AM
 * @Homepage : https://github.com/gusdnd852
 */
public class FairytaleViewModel extends BaseViewModel {

    public FairytaleViewModel(@NonNull Application application) {
        super(application);
    }

    public void onCreate() {
        Observable.just(null)
                .subscribeOn(RxSchedulers.networkThread())
                .doOnNext(Null -> Fairytale.get().play())
                .observeOn(RxSchedulers.androidThread())
                .subscribe();
    }

    @Override protected void onCleared() {
        super.onCleared();
    }
}
