package com.welfarerobotics.welfareapplcation.ui.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.widget.Toast;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.model.ConversationModel;
import com.welfarerobotics.welfareapplcation.model.UserSingleton;
import com.welfarerobotics.welfareapplcation.ui.base.BaseActivity;
import org.jsoup.Connection;

import java.util.ArrayList;
import java.util.Objects;

public class ConversationEdit extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_edit);
        UserSingleton singleton = UserSingleton.getInstance();
        ArrayList<ConversationModel> dict = Objects.requireNonNull(singleton).getDict();
    }
}
