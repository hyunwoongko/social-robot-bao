package com.welfarerobotics.welfareapplcation.core.splash;

import android.app.Application;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseViewModel;
import com.welfarerobotics.welfareapplcation.util.LiveData;
import com.welfarerobotics.welfareapplcation.util.MethodOnXml;
import com.welfarerobotics.welfareapplcation.util.Sound;
import lombok.Getter;
import rx.Observable;

/**
 * @Author : Hyunwoong
 * @When : 4/23/2019 10:09 PM
 * @Homepage : https://github.com/gusdnd852
 */
public @Getter class SplashViewModel extends BaseViewModel {

    private LiveData moveToTutorialActivityEvent = new LiveData();
    private LiveData moveToMainActivityEvent = new LiveData();
    private LiveData<String> uuidData = new LiveData<>();
    private LiveData<Boolean> isFirstUser = new LiveData<>();

    private SplashModel model = SplashModel.builder()
            .moveToTutorialActivityEvent(moveToTutorialActivityEvent)
            .moveToMainActivityEvent(moveToMainActivityEvent)
            .uuid(uuidData)
            .build();

    public SplashViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        Observable.just(model)
                .doOnNext(m -> Sound.get().start(getContext(), R.raw.intro)) // 효과음 재생
                .doOnNext(SplashModel::getApiKeys) // api 로드
                .map(m -> isFirstUser.getValue()) // 최초유저 검사
                .doOnNext(firstUser -> { // 스플래시 액션
                    if (firstUser) model.firstUserSplashEvent();
                    else model.readyMadeUserSplashEvent();
                }).subscribe(); // 구독
    }

    @MethodOnXml
    public void welfareLogoFadeIn(View view) {
        Animation fadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.logo_fade_in);
        view.startAnimation(fadeInAnimation);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}