package com.welfarerobotics.welfareapplcation.core.contents.dictation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.myscript.atk.scw.SingleCharWidget;
import com.myscript.atk.scw.SingleCharWidgetApi;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.BaseActivity;

public class DicationActivity extends BaseActivity implements  SingleCharWidgetApi.OnTextChangedListener{
private MyScriptBuilder builder;
private SingleCharWidgetApi widget;
private String answer;
    TextView textView ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictationgame);


        textView  = findViewById(R.id.test);


        ImageButton submit = findViewById(R.id.submit_btn);
        ImageButton clear = findViewById(R.id.clear_btn);
        submit.setClickable(true);
        clear.setClickable(true);


        widget = (SingleCharWidget)findViewById(R.id.widget);
        widget.setOnTextChangedListener(this);
        builder= new MyScriptBuilder(widget, this);
        builder.Build();
        widget.setInkFadeOutEffect(1);

        /*위젯 설정*/

        submit.setOnClickListener(view->{
            Toast.makeText(this, "입력한 단어"+textView.getText().toString(), Toast.LENGTH_SHORT).show();

        });

        clear.setOnClickListener(view->{
            Toast.makeText(this,"초기화합니다.",Toast.LENGTH_SHORT).show();
            widget.clear();
            textView.setText("");
        });

    }



    @Override
    public void onTextChanged(SingleCharWidgetApi singleCharWidgetApi, String s, boolean b) {
            textView.setText(s);
    }




}
