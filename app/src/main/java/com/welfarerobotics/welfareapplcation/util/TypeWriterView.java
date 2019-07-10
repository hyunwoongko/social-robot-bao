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
import android.util.Log;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class TypeWriterView extends TextView {
//글씨를 타자기처럼 한글자 한글자 순서대로 적어주는 커스텀뷰

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 100; // ms
    private Handler mHandler = new Handler();

/*원우 추가*/
    private boolean autoSize = false;
    private float max;
    private float min;
    private float degree;
/*원우 추가*/

    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {


            setText(mText.subSequence(0, mIndex++));

            if (mIndex <= mText.length()) {



                mHandler.postDelayed(characterAdder, mDelay);

                /*원우 추가*/
                startAutoSizing();
                if(autoSize==true){
                    if(mIndex==1){
                        setTextSize(max);
                    }

                }

                /*원우 추가*/


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

    /*원우 추가*/
    public void setAuto(boolean autoSize){
        final float defaultMin = 30.0f;
        final float defaultMax = 40.0f;
        setAuto(autoSize,defaultMin,defaultMax);

    }

    public void setAuto(boolean autoSize,float min,float max){
        final float  defaultDegree = 2.0f;

        setAuto(autoSize,min,max,defaultDegree);

    }

    public void setAuto(boolean autoSize,float min,float max,float degree){
        this.autoSize = autoSize;
        this.min = min;
        this.max =max;
        this.degree=degree;
        setTextSize(max);

    }

/*사이징 변경 중에 새로운 텍스트가 들어오면 안되므로 동기화 처리.*/
    private synchronized  void startAutoSizing(){
        if(autoSize==true){

            float fontsize = getTextSize()/ getResources().getDisplayMetrics().scaledDensity;//getTextSize는 px를 받아옴. sp로 바꿔주는 연산
            float minsize = min/ getResources().getDisplayMetrics().scaledDensity;
            int textSize = new Float(getText().length()*fontsize).intValue();//전체 텍스트 길이
            if(textSize>getWidth()) {

                if ((fontsize- degree) > minsize) {
                    Log.d("폰트 크기 변경 전",""+getTextSize());
                    float setSize = fontsize-degree;//텍스트 사이즈가 텍스트뷰 넓이보다 크면 degree만큼 줄임(최저값 이내로.)
                    Log.d("폰트 크기 변경 값", ""+(setSize));
                    setTextSize(setSize);
                    Log.d("폰트 크기 변경 후", ""+(getTextSize()));

                }
            }
            Log.d("텍스트 크기 테스트","TextSize"+getText().length()*getTextSize());
            Log.d("텍스트 뷰 크기 테스트","TextViewSize"+getWidth());

        }

    }

    /*원우 추가*/
}