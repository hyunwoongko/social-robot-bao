package com.welfarerobotics.welfareapplcation.bot.Body;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.util.bluetooth.Bluetooth;

import java.util.ArrayList;

public class Dance {

    public Dance(){
        ArrayList<ArrayList<String>> commands =DanceCashe.getInstance().getCommands();
        ArrayList<String> command = commands.get(0);
        Thread thread = new Thread(()->{
            for(int i =0; i<command.size();i++){
                Bluetooth.getInstance().sendMessage(command.get(i));
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        });
        thread.start();

    }

}
