package com.welfarerobotics.welfareapplcation.bot.brain.chat.state;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.Mouth;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/24/2019 4:46 PM
 * @homepage : https://github.com/gusdnd852
 * <p>
 * State Pattern 으로 구현
 */
public interface ChatState {
    ContextState CONTEXT_STATE = ContextState.getInstance();
    NormalState NORMAL_STATE = NormalState.getInstance();
    QuestionState QUESTION_STATE = QuestionState.getInstance();
    FallbackState FALLBACK_STATE = FallbackState.getInstance();

    ChatState think(String intent, String speech, Activity activity) throws IOException;
    ChatState speech(Mouth voice);
}
