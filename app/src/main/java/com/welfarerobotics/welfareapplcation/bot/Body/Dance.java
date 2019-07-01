package com.welfarerobotics.welfareapplcation.bot.Body;

import com.welfarerobotics.welfareapplcation.util.bluetooth.Bluetooth;

import java.util.ArrayList;

public class Dance {

    public Dance(){
        ArrayList<ArrayList<String>> commands = DanceCache.getInstance().getCommands();
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
