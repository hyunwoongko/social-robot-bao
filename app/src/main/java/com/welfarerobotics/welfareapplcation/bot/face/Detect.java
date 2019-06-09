package com.welfarerobotics.welfareapplcation.bot.face;

import android.os.Handler;
import android.os.Message;

public class Detect {

    public Detect(Handler handler){


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{

                        Message msg = handler.obtainMessage();
                        Thread.sleep(1000);
                        handler.sendMessage(msg);
                    }catch (Exception e){


                    }


                }


            }
        });
        t.start();


    }

}


