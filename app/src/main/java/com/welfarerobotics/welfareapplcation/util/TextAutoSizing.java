package com.welfarerobotics.welfareapplcation.util;

import android.widget.TextView;

public class TextAutoSizing {
    private TextView textView;
    private int defalutMax = 30;
    private int defalutMin = 10;
    private int defaultSize =20;

    public TextAutoSizing(TextView textView){
        this.textView = textView;

    }

    public void setText(String text){
        setText(text,defaultSize);

    }
    public void setText(String text,int size){
        setText(text,size,defalutMax,defalutMin);


    }
    public void setText(String text,int size,int max,int min){

        setText(textView,text,size,max,min);

    }

    public void setText(TextView textView,String text,int size,int max,int min){
        int textSize = new Float(textView.getTextSize()).intValue();
        textView.getMaxWidth();



    }



    public void setTextView(TextView textView) {
        this.textView = textView;
    }


}
