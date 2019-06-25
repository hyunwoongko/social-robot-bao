package com.welfarerobotics.welfareapplcation.core.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.entity.Conversation;
import com.welfarerobotics.welfareapplcation.entity.User;
import com.welfarerobotics.welfareapplcation.entity.cache.UserCache;
import com.welfarerobotics.welfareapplcation.util.DeviceId;
import com.welfarerobotics.welfareapplcation.util.NetworkUtil;
import es.dmoral.toasty.Toasty;

import java.util.ArrayList;

public class ConversationEdit extends BaseActivity {
    private EditText question;
    private EditText answer;
    private ImageView saveview;
    private LinearLayout listview;
    private String qstring;
    private String astring;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_edit);
        question = findViewById(R.id.question_edit);
        answer = findViewById(R.id.answer_edit);
        saveview = findViewById(R.id.save_view);
        listview = findViewById(R.id.listView);
        saveview.setOnClickListener(v -> {
            qstring = question.getText().toString().trim();
            astring = answer.getText().toString().trim();

            if (qstring.replaceAll(" ", "").equals(""))
                Toasty.error(this, "질문을 입력하세요", Toast.LENGTH_SHORT).show();
            else if (astring.replaceAll(" ", "").equals(""))
                Toasty.error(this, "대답을 입력하세요", Toast.LENGTH_SHORT).show();
            else {
                String id = DeviceId.getInstance(this).getUUID();
                User model = new User();
                model.setId(UserCache.getInstance().getId());
                model.setName(UserCache.getInstance().getName());
                model.setLocation(UserCache.getInstance().getLocation());
                Conversation convModel = new Conversation();

                if (UserCache.getInstance().getDict() == null) {
                    UserCache.getInstance().setDict(new ArrayList<>());
                }

                boolean canSave = true;
                for (Conversation c : UserCache.getInstance().getDict()) {
                    System.out.println("DEBUG - 1: " + c.getInput().trim().replaceAll(" ", ""));
                    System.out.println("DEBUG - 2 : " + qstring.trim().replaceAll(" ", ""));

                    if (c.getInput().trim().replaceAll(" ", "").equals(qstring.trim().replaceAll(" ", ""))) {
                        Toasty.error(this, "이미 배운 말이에요 !", Toast.LENGTH_SHORT).show();
                        canSave = false;
                    }
                }
                if (canSave) {
                    convModel.setInput(qstring);
                    convModel.setOutput(astring);
                    UserCache.getInstance().getDict().add(convModel);
                    Toasty.success(this, "성공적으로 말을 배웠어요 !", Toast.LENGTH_SHORT).show();

                    NetworkUtil.wifiSafe(this, () -> {
                        //UserSingleton 업데이트
                        model.setDict(UserCache.getInstance().getDict());
                        FirebaseDatabase.getInstance()
                                .getReference("user")
                                .child(id)
                                .setValue(model);
                        // 디비 업로드
                    });
                }
            }
        });
        listview.setOnClickListener(v -> {
            Intent intent = new Intent(this, ConversationList.class);
            startActivity(intent);
        });
        findViewById(R.id.backbutton).setOnClickListener(v -> finish());
    }
}
