package com.welfarerobotics.welfareapplcation.api.chat;

import android.support.v7.app.AppCompatActivity;
import com.welfarerobotics.welfareapplcation.model.UserModel;
import com.welfarerobotics.welfareapplcation.api.chat.chatutil.ChatState;
import com.welfarerobotics.welfareapplcation.api.chat.session.*;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 3/23/2019 10:58 PM
 * @Homepage : https://github.com/gusdnd852
 */
public final class ChatApi {
    private static ChatApi api = null;

    private ChatApi() {
    }

    public synchronized static ChatApi get() {
        if (api == null) api = new ChatApi();
        return api;
    }

    public synchronized void chat(String speech, UserModel model, AppCompatActivity activity) {
        try {
            if (ChatState.questionMode) QuestionSession.questionProcess(speech); // 질문 세션
            else {
                PreprocessingSession.preprocess(speech); // 전처리 세션
                ContextSession.contextProcess(); // 문맥판단 세션
                ProposedTalkingSession.proposedTalk(model, activity); // 목적 대화 세션
                NonProposedTalkingSession.nonProposedTalk(model); // 비목적 대화 세션
            }
        } catch (IOException e) {
            ExceptionSession.except(e); // 예외처리 세션
        }
    }
}
