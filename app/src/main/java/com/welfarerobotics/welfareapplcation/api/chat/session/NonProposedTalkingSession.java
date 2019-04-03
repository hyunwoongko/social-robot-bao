package com.welfarerobotics.welfareapplcation.api.chat.session;

import com.welfarerobotics.welfareapplcation.model.UserModel;
import com.welfarerobotics.welfareapplcation.api.chat.chatutil.ChatState;
import com.welfarerobotics.welfareapplcation.api.chat.CssApi;
import com.welfarerobotics.welfareapplcation.api.chat.chatutil.EmotionAdder;
import com.welfarerobotics.welfareapplcation.api.chat.chatutil.NameReplacer;
import com.welfarerobotics.welfareapplcation.util.RandomModule;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.ModelApi;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.PreprocessorApi;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 4/3/2019 11:45 AM
 * @Homepage : https://github.com/gusdnd852
 *
 * 비 목적 대화 세션
 */
public final class NonProposedTalkingSession {
    /*TODO : 시나리오 이 것보다 더 많이 짜야함*/
    public static void nonProposedTalk(UserModel model) throws IOException {
        if (ChatState.intent.equals("우울") || ChatState.intent.equals("슬픔")) {
            int prob = RandomModule.random.nextInt(3);
            if (prob == 1) {
                String joke = "";
                CssApi.get().play("혹시 지금 기분이 안 좋으신가요? , 제가 재밌는 이야기를 들려드릴게요. , " + joke, "jinho");
                //TODO : 농담 API 연결
            } else {
                ChatState.contextGeneratedAnswer = PreprocessorApi.fix(ModelApi.generateAnswer(model.getUserId(), ChatState.fixedSpeech));
                ChatState.contextGeneratedAnswer = NameReplacer.replaceName(model, ChatState.contextGeneratedAnswer);
                CssApi.get().play(ChatState.contextGeneratedAnswer, "jinho");
            }
        } else if (ChatState.contextIntent.equals("양자택일")) {
            if (ChatState.intent.equals("긍정대답")) {
                ChatState.contextGeneratedAnswer = PreprocessorApi.fix(ModelApi.generateAnswer(model.getUserId(), ChatState.fixedSpeech));
                ChatState.contextGeneratedAnswer = NameReplacer.replaceName(model, ChatState.contextGeneratedAnswer);
                CssApi.get().play(ChatState.contextGeneratedAnswer + " , 그래서 결정은 하셨나요?", "jinho");
            } else if (ChatState.intent.equals("부정대답")) {
                ChatState.contextGeneratedAnswer = PreprocessorApi.fix(ModelApi.generateAnswer(model.getUserId(), ChatState.fixedSpeech));
                ChatState.contextGeneratedAnswer = NameReplacer.replaceName(model, ChatState.contextGeneratedAnswer);
                CssApi.get().play(ChatState.contextGeneratedAnswer + " , 그래서 아직 결정 못하셨나요?", "jinho");
            } else {
                ChatState.contextGeneratedAnswer = PreprocessorApi.fix(ModelApi.generateAnswer(model.getUserId(), ChatState.fixedSpeech));
                ChatState.contextGeneratedAnswer = NameReplacer.replaceName(model, ChatState.contextGeneratedAnswer);
                ChatState.contextGeneratedAnswer = EmotionAdder.applyEmotion(ChatState.contextGeneratedAnswer);
                CssApi.get().play(ChatState.contextGeneratedAnswer, "jinho");
            }
        } else {
            ChatState.contextGeneratedAnswer = PreprocessorApi.fix(ModelApi.generateAnswer(model.getUserId(), ChatState.fixedSpeech));
            ChatState.contextGeneratedAnswer = NameReplacer.replaceName(model, ChatState.contextGeneratedAnswer);
            ChatState.contextGeneratedAnswer = EmotionAdder.applyEmotion(ChatState.contextGeneratedAnswer);
            CssApi.get().play(ChatState.contextGeneratedAnswer, "jinho");
        }
    }
}
