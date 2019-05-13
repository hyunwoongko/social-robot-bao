package com.welfarerobotics.welfareapplcation.api.chat.chatutil;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author : Hyunwoong
 * @When : 4/3/2019 11:38 AM
 * @Homepage : https://github.com/gusdnd852
 */
public final class ChatState {
    // 전처리 텍스트
    public static String speech;

    // 의도큐
    public static ConcurrentLinkedQueue<String> intentQueue = new ConcurrentLinkedQueue<>();

}