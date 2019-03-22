package com.welfarerobotics.welfareapplcation.chat_api;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 3/22/2019 3:29 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ModelApi modelApi = new ModelApi();
        String res = modelApi.getEntity("가 나");
        System.out.println(res);
    }
}
