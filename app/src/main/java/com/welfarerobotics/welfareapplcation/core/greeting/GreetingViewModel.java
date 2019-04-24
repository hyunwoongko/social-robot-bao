package com.welfarerobotics.welfareapplcation.core.greeting;

import android.app.Application;
import android.support.annotation.NonNull;
import android.view.View;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseViewModel;
import com.welfarerobotics.welfareapplcation.util.LiveData;
import com.welfarerobotics.welfareapplcation.util.Sound;
import com.welfarerobotics.welfareapplcation.util.TypeWriterView;
import lombok.Getter;

/**
 * @Author : Hyunwoong
 * @When : 4/24/2019 12:32 PM
 * @Homepage : https://github.com/gusdnd852
 */
public @Getter class GreetingViewModel extends BaseViewModel {

    private LiveData showDialogEvent = new LiveData();
    private LiveData<TypeWriterView> typeWriterView = new LiveData<>();
    private LiveData moveToUserSettingActivityEvent = new LiveData();

    private int msgCount = 0;
    private String[] msg = {
            "안녕하세요.",
            "저는 인공지능 로봇 바오라고 합니다.",
            "제게 사용자님이 누구인지 알려주세요."
    };

    public GreetingViewModel(@NonNull Application application) {
        super(application);
    }

    @Override public void onCreate() {
        super.onCreate();
        showDialogEvent.call();
        typeWriterView.getValue().setText(msg[msgCount]);
    }

    public void nextButtonClicked(View view) {
        msgCount++; // 대화 카운트 증가
        Sound.get().effectSound(getContext(), R.raw.click);
        if (msgCount >= msg.length) moveToUserSettingActivityEvent.call();
        else typeWriterView.getValue().write(msg[msgCount], 70);
    }

    @Override protected void onCleared() {
        super.onCleared();
    }
}
