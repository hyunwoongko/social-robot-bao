package com.welfarerobotics.welfareapplcation.core.base;

import android.arch.lifecycle.AndroidViewModel;

/**
 * @Author : Hyunwoong
 * @When : 4/24/2019 12:34 AM
 * @Homepage : https://github.com/gusdnd852
 * <p>
 * 생명주기 정의
 */
public interface BaseLifeCycle {

    // 뷰 설계
    void onCreateView(int layout, Class<? extends AndroidViewModel> clazz);

    // 리스너추가
    default void addListener() {
    }

    // 데이터 정의
    default void observeData() {
    }

    // 액션 정의
    default void observeAction() {
    }
}
