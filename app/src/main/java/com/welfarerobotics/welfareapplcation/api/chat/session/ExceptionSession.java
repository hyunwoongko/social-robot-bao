package com.welfarerobotics.welfareapplcation.api.chat.session;

import com.welfarerobotics.welfareapplcation.api.chat.CssApi;
import com.welfarerobotics.welfareapplcation.api.chat.chatutil.ChatState;

/**
 * @Author : Hyunwoong
 * @When : 4/3/2019 12:17 PM
 * @Homepage : https://github.com/gusdnd852
 *
 * 예외처리 세션
 */
public class ExceptionSession {
    public static void except(Throwable e) {
        ChatState.contextGeneratedAnswer = "죄송해요. 아직 배우는 중이라, 잘 이해하지 못했어요. 다시 말씀해주시겠어요?";
        CssApi.get().play(ChatState.contextGeneratedAnswer, "jinho");
        e.printStackTrace(); // 예외처리 세션
    }
}
