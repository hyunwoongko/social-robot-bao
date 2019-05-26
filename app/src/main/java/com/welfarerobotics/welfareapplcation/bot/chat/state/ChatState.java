package com.welfarerobotics.welfareapplcation.bot.chat.state;

import com.welfarerobotics.welfareapplcation.bot.chat.intent.ChatIntent;

/**
 * @author : Hyunwoong
 * @when : 5/24/2019 4:46 PM
 * @homepage : https://github.com/gusdnd852
 * <p>
 * State Pattern 으로 구현
 */
@FunctionalInterface
public interface ChatState {
    ContextState CONTEXT_STATE = ContextState.getInstance();
    NormalState NORMAL_STATE = NormalState.getInstance();
    QuestionState QUESTION_STATE = QuestionState.getInstance();

    ChatState answer(ChatIntent intent);
}
