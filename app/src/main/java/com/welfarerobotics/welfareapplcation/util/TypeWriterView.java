package com.welfarerobotics.welfareapplcation.util;

/**
 * @Author : Hyunwoong
 * @When : 2018-09-23 오후 2:59
 * @Homepage : https://github.com/gusdnd852
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class TypeWriterView extends TextView {
//글씨를 타자기처럼 한글자 한글자 순서대로 적어주는 커스텀뷰

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 100; // ms
    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if (mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    public TypeWriterView(Context context) {
        super(context);
    }


    public TypeWriterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void animateText(CharSequence txt) {
        mText = txt;
        mIndex = 0;
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    private void setCharacterDelay(long m) {
        mDelay = m;
    }


    public void write(String text) {
        setText(""); // 글자를 지워줌
        setCharacterDelay(0); // 딜레이값 설정
        animateText(text); // 글씨를 한글자씩 적어주는 메소드
    }

    public void write(String text, int speed) {
        setText(""); // 글자를 지워줌
        setCharacterDelay(speed); // 딜레이값 설정
        animateText(text); // 글씨를 한글자씩 적어주는 메소드
    }
}