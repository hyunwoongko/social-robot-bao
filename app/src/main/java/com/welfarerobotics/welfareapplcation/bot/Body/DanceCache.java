package com.welfarerobotics.welfareapplcation.bot.Body;

import java.util.ArrayList;

public class DanceCache {

    static private DanceCache danceCashe=null;
    private ArrayList<ArrayList<String>> Commands = new ArrayList<ArrayList<String>>();
    static public DanceCache getInstance(){
        if(danceCashe==null){
            danceCashe = new DanceCache();

        }

        return danceCashe;
    }

    private DanceCache(){
    }

    public void setCommands(ArrayList<ArrayList<String>> commands) {
        Commands = commands;
    }

    public void addCommans(ArrayList<String>command){
        Commands.add(command);
    }


    public ArrayList<ArrayList<String>> getCommands(){
        return  Commands;
    }
}
