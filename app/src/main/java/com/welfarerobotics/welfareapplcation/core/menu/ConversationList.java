package com.welfarerobotics.welfareapplcation.core.menu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.entity.Conversation;
import com.welfarerobotics.welfareapplcation.entity.User;
import com.welfarerobotics.welfareapplcation.entity.cache.UserCache;
import com.welfarerobotics.welfareapplcation.util.DeviceId;

import java.util.ArrayList;

public class ConversationList extends BaseActivity {
    private String a;
    private String b;
    private TextView question;
    private static final float FONT_SIZE = 10;
    private GridLayout topLL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_list);
        topLL = (GridLayout) findViewById(R.id.Gridlayout);

        if (UserCache.getInstance().getDict() == null) {
            ArrayList<Conversation> conversations = new ArrayList<>();
            Conversation conversation = new Conversation();
            conversation.setInput("안녕");
            conversation.setOutput("안녕하세요. 반가워요.");
            conversations.add(conversation);

            User model = User.builder()
                    .id(UserCache.getInstance().getId())
                    .name(UserCache.getInstance().getName())
                    .location(UserCache.getInstance().getLocation())
                    .photo(UserCache.getInstance().getPhoto())
                    .dict(conversations)
                    .build();

            FirebaseDatabase.getInstance()
                    .getReference("user")
                    .child(DeviceId.getInstance(this).getUUID())
                    .setValue(model);

            UserCache.setInstance(model);
        }

        ArrayList<Conversation> dict = UserCache.getInstance().getDict();

        if (dict.size() != 0) {
            for (Conversation once : dict) {
                textview(once.getInput());
                textview(once.getOutput());
            }
        } else
            Toast.makeText(ConversationList.this, "아직 배운 말이 없어요.", Toast.LENGTH_SHORT).show();
//        FirebaseDatabase.getInstance()
//                .getReference("user")
//                .child("user1")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        UserModel model = dataSnapshot.getValue(UserModel.class);
//                        UserSingleton singleton = UserSingleton.setInstance(model);
//                        ArrayList<ConversationModel> dict = Objects.requireNonNull(singleton).getDict();
//                        if (dict.size() != 0) {
//                            for (ConversationModel once : dict) {
//                                textview(once.getInput());
//                                textview(once.getOutput());
//                            }
//                        } else
//                            Toast.makeText(ConversationActivity.this, "아직 배운 말이 없어요.", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
    }

    public void textview(String a) {
        //TextView 생성
        TextView view1 = new TextView(this);
        view1.setPadding(20, 10, 10, 10);
        view1.setText(a);
        view1.setTextSize(FONT_SIZE);
        view1.setTextColor(Color.BLACK);
        //topTV1.setTextColor(Color.parseColor("#FF7200"));
        //topTV1.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        //부모 뷰에 추가
        topLL.addView(view1);
    }
}
