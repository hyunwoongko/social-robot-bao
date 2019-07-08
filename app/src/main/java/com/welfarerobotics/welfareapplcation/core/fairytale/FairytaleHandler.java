package com.welfarerobotics.welfareapplcation.core.fairytale;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.welfarerobotics.welfareapplcation.util.TypeWriterView;

public class FairytaleHandler extends Handler {
    private TypeWriterView writerView;
    public FairytaleHandler(TypeWriterView writerView){

        this.writerView = writerView;
    }

    public void write(String context){

        writerView.write(context);

    }
    public void write(String context,int speed,int what){

        super.sendEmptyMessage(what);
//        writerView.write(context,speed);

    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        writerView.write(msg.obj.toString(),90);


    }



}
