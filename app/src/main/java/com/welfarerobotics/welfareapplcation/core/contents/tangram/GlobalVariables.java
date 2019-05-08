package com.welfarerobotics.welfareapplcation.core.contents.tangram;
import android.app.Application;


public class GlobalVariables extends Application {
	private static int currentLevel = 1;
	public static boolean outlineOn=true;
	public static void setCurrentLevel(int level){
		currentLevel = level;
	}
	public static int getCurrentLevel(){
		return currentLevel;
	}
	
	/*the getter for the highScore*/
/*지워도 되긴 하는데 일단 나중에 사용할 수도 있어서 남겨둠.*/

}
