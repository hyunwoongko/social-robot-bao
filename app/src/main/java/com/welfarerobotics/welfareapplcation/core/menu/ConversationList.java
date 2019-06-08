package com.welfarerobotics.welfareapplcation.core.menu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;
import com.kinda.alert.KAlertDialog;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.entity.Conversation;
import com.welfarerobotics.welfareapplcation.entity.cache.UserCache;
import com.welfarerobotics.welfareapplcation.util.DeviceId;
import es.dmoral.toasty.Toasty;

import java.util.ArrayList;

public class ConversationList extends BaseActivity {
    private String a;
    private String b;
    private TextView question;
    private GridLayout topLL;
    private ArrayList<Conversation> dict;
    private int num = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_list);
        topLL = (GridLayout) findViewById(R.id.Gridlayout);
        topLL.setVerticalScrollBarEnabled(true);
        topLL.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);

        UserCache singleton = UserCache.getInstance();
        singleton.setDict(singleton.getDict() == null ? new ArrayList<>() : singleton.getDict());
        if (singleton.getDict().size() != 0) {
            for (Conversation once : singleton.getDict()) {
                textview(once.getInput(), true);
                textview(once.getOutput(), false);
            }
        } else
            Toasty.warning(ConversationList.this, "아직 배운 말이 없어요.", Toast.LENGTH_SHORT).show();


        findViewById(R.id.backbutton).setOnClickListener(v -> {
            finish();
        });

    }

    public void textview(String a, boolean isQuesiton) {
        num++;
        //TextView 생성
        TextView view1 = new TextView(this);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.baemin);
        view1.setTypeface(typeface);
        view1.setPadding(20, 10, 10, 10);
        view1.setText(a);
        view1.setId(num);
        view1.setTextSize(14);
        view1.setTextColor(Color.BLACK);
        if (isQuesiton) {
            view1.setOnClickListener(v -> {
                KAlertDialog pDialog = new KAlertDialog(this);
                pDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText("배운 말 잊기");
                pDialog.setContentText("'" + a + "'를 잊을까요?");
                pDialog.setCancelable(true);
                pDialog.setCancelText("취소");
                pDialog.setConfirmText("잊기");
                pDialog.setConfirmClickListener(l -> {
                    ArrayList<Conversation> list = UserCache.getInstance().getDict();
                    ArrayList<Conversation> listToForget = new ArrayList<>();
                    for (Conversation c : list) {
                        if (c.getInput().equals(a)) {
                            listToForget.add(c);
                        }
                    }
                    for (Conversation c : listToForget) {
                        list.remove(c);
                    }
                    UserCache.getInstance().setDict(list);
                    FirebaseDatabase.getInstance()
                            .getReference("user")
                            .child(DeviceId.getInstance(this).getUUID())
                            .child("dict")
                            .setValue(list);

                    pDialog.dismissWithAnimation();
                    Toasty.success(this, "성공적으로 잊었어요 !", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(this, ConversationList.class));
                });
                pDialog.setCancelClickListener(l -> {
                    pDialog.dismissWithAnimation();
                });
                pDialog.show();
            });
        }
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        param.height = GridLayout.LayoutParams.WRAP_CONTENT;
        param.width = GridLayout.LayoutParams.WRAP_CONTENT;
        view1.setLayoutParams(param);
        topLL.addView(view1);
    }
}
