package com.welfarerobotics.welfareapplcation.chat_scenario;

import java.util.Scanner;

/**
 * @Author : Hyunwoong
 * @When : 3/22/2019 4:38 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class Voice {
    /*
     * 일단 tts는 system.out.println으로
     * stt는 Scanner로 설정해서 텍스트 대화 가능하게 해놓음.
     * */

    public static void tts(String text) {
        System.out.println(text);
    }

    public static String stt() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
