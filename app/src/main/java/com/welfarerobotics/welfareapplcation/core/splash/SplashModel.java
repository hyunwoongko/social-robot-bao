package com.welfarerobotics.welfareapplcation.core.splash;

import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.core.base.BaseModel;
import com.welfarerobotics.welfareapplcation.entity.Server;
import com.welfarerobotics.welfareapplcation.entity.ServerCache;
import com.welfarerobotics.welfareapplcation.entity.User;
import com.welfarerobotics.welfareapplcation.entity.UserCache;
import com.welfarerobotics.welfareapplcation.util.FirebaseHelper;
import com.welfarerobotics.welfareapplcation.util.LiveData;
import com.welfarerobotics.welfareapplcation.util.RxSchedulers;
import lombok.Builder;
import rx.Observable;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author : Hyunwoong
 * @When : 4/23/2019 10:45 PM
 * @Homepage : https://github.com/gusdnd852
 */
@Builder class SplashModel extends BaseModel {

    private LiveData moveToMainActivityEvent;
    private LiveData moveToTutorialActivityEvent;
    private LiveData<String> uuid;

    /**
     * 서버에서 각종 API 키들을 로드합니다.
     */
    void getApiKeys() {
        FirebaseHelper.get().connect(FirebaseDatabase.getInstance()
                .getReference("server"), dataSnapshot ->
                ServerCache.setInstance(dataSnapshot.getValue(Server.class)));
    }

    /**
     * 최초사용자의 경우 몇초간 화면을 멈추고
     * 다음으로 진행합니다.
     */
    void firstUserSplashEvent() {
        Observable.just(null)
                .subscribeOn(RxSchedulers.backgroundThread())
                .delay(3500, TimeUnit.MILLISECONDS)
                .observeOn(RxSchedulers.androidThread())
                .subscribe(Null -> moveToTutorialActivityEvent.call());
    }

    /**
     * 기성사용자의 경우 데이터베이스에서
     * 값을 로드한 뒤 다음으로 진행합니다.
     */
    void readyMadeUserSplashEvent() {
        FirebaseHelper.get().connect(FirebaseDatabase.getInstance()
                .getReference("user")
                .child(Objects.requireNonNull(uuid.getValue())), dataSnapshot ->
                Observable.just(dataSnapshot.getValue(User.class))
                        .subscribeOn(RxSchedulers.backgroundThread())
                        .delay(2500, TimeUnit.MILLISECONDS)
                        .doOnNext(UserCache::setInstance)
                        .observeOn(RxSchedulers.androidThread())
                        .subscribe(m -> moveToMainActivityEvent.call()));
    }
}
