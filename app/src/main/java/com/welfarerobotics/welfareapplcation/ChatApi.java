package com.welfarerobotics.welfareapplcation;

import com.welfarerobotics.welfareapplcation.chat_api.ModelApi;
import com.welfarerobotics.welfareapplcation.chat_api.PreprocessorApi;
import com.welfarerobotics.welfareapplcation.chat_scenario.ScenarioDust;
import com.welfarerobotics.welfareapplcation.chat_scenario.Voice;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 3/22/2019 5:12 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class ChatApi {

    //일단 자바로 테스트함. 나중에 안드로이드로 그대로 옮기면 됨.
    public static void main(String[] args) throws IOException {
        ScenarioDust dust = new ScenarioDust();
        // 시나리오 생성

        PreprocessorApi preprocessor = new PreprocessorApi();
        // 프리프로세서 생성

        ModelApi modelApi = new ModelApi();
        // 모델 생성

        while (true) {
            Voice.tts("입력하세요");
            String usersSaying = Voice.stt();
            usersSaying = preprocessor.tokenize(usersSaying);
            usersSaying = preprocessor.fix(usersSaying);

            String intent = modelApi.getIntent(usersSaying);
            System.out.println("발화 의도 : " + intent);
            // 유저의 발화의도 파악

            if (intent.equals("먼지")) {
                List<String[]> recognizedEntity = modelApi.getEntity(usersSaying);
                for (String[] one : recognizedEntity) System.out.println("개체명 인식 : " + Arrays.toString(one));
                Voice.tts(dust.response(recognizedEntity));
                //일단 미세먼지 시나리오만 구현함
            } else {
                Voice.tts("아직 준비 중 입니다.");
            }
        }
    }
}
