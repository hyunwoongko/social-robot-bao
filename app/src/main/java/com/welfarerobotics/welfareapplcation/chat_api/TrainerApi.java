package com.welfarerobotics.welfareapplcation.chat_api;

import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * @Author : Hyunwoong
 * @When : 3/22/2019 2:28 PM
 * @Homepage : https://github.com/gusdnd852
 * TODO : Transformer 모델 학습기 API 만들어야함.
 */

public final class TrainerApi {
    /**
     * Char - CNN 모델 학습기 API
     * 발화의도 분류기 모델을 학습함.
     */
    public void trainIntent() throws IOException {
        Jsoup.connect(ApiServer.SERVER_URL + "/train_intent")
                .get()
                .body()
                .text();
    }

    /**
     * BiLSTM - CRF 모델 학습기 API
     * 개체명 인식기 모델을 학습함.
     */
    public void trainEntity() throws IOException {
        Jsoup.connect(ApiServer.SERVER_URL + "/train_entity")
                .get()
                .body()
                .text();
    }
}
