package com.welfarerobotics.welfareapplcation.api.chat.tools;

import com.welfarerobotics.welfareapplcation.api.chat.crawler.ModelApi;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 5/13/2019 8:13 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class IntentClassifier {

    public static String getIntent(String speech) throws IOException {
        String intent = ModelApi.getIntent(speech); // intent classifier 로 인텐트 체크
        if (intent.equals("폴백")) intent = ModelApi.getSimilarity(speech); // 폴백인 경우 유사도 검사고 인텐트 체크
        if (!intent.equals("폴백")) ChatState.intentQueue.add(intent); // 그래도 폴백인 경우를 제외하고 큐에 저장
        return intent;
    }
}
