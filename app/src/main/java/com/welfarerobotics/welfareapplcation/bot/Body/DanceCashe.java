package com.welfarerobotics.welfareapplcation.bot.Body;

import java.util.ArrayList;

public class DanceCashe {

    static private DanceCashe danceCashe=null;
    private ArrayList<ArrayList<String>> Commands = new ArrayList<ArrayList<String>>();
    static public DanceCashe getInstance(){
        if(danceCashe==null){
            danceCashe = new DanceCashe();

        }

        return danceCashe;
    }

    private DanceCashe(){


    }

    public void addCommans(ArrayList<String>command){
        Commands.add(command);
    }
    public ArrayList<ArrayList<String>> getCommands(){
        return  Commands;
    }
}
