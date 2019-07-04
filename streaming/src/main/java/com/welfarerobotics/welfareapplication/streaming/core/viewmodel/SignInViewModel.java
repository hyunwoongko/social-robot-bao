package com.welfarerobotics.welfareapplication.streaming.core.viewmodel;

import android.arch.lifecycle.ViewModel;
import com.welfarerobotics.welfareapplication.streaming.util.binding.ViewData;
import lombok.Getter;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 1:11
 * @homepage : https://github.com/gusdnd852
 */

@Getter
public class SignInViewModel extends ViewModel {

    private ViewData<String> uuid = new ViewData<>();

}
