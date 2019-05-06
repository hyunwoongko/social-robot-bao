package com.welfarerobotics.welfareapplcation.core.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.BaseActivity;
import com.welfarerobotics.welfareapplcation.entity.Conversation;
import com.welfarerobotics.welfareapplcation.entity.UserCache;

import java.util.ArrayList;
import java.util.Objects;

public class ConversationEdit extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_edit);
        UserCache singleton = UserCache.getInstance();
        ArrayList<Conversation> dict = Objects.requireNonNull(singleton).getDict();
    }
}
