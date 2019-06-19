package com.welfarerobotics.welfareapplcation.bot.Body;

public enum Signal {
    LeftArm,Neck,RightArm;

    public String toString() {
        switch (this){
            case LeftArm: return "Movelef";
            case Neck: return "Movenec";
            case RightArm: return "Moverit";
            default: return "NULL";
        }
    }

}
