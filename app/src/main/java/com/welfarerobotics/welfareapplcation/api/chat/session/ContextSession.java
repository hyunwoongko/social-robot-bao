package com.welfarerobotics.welfareapplcation.api.chat.session;

import com.welfarerobotics.welfareapplcation.api.chat.chatutil.ChatState;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.ModelApi;
import com.welfarerobotics.welfareapplcation.api.chat.scenario.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 4/3/2019 11:45 AM
 * @Homepage : https://github.com/gusdnd852
 * <p>
 * 문맥판단 세션
 */
public final class ContextSession {
    public static boolean contextProcess() throws IOException {
        if (!ChatState.intent.contains("문맥")) {
            ChatState.contextIntent = ChatState.intent; // 문맥관련 의도가 아닐경우에만 이전 의도를 저장
        } else if (ChatState.intent.equals("날씨먼지문맥")) {


            if (ChatState.contextIntent.equals("먼지")) {
                String[][] entity = ModelApi.getEntity("dust", ChatState.tokenizeSpeech);
                List<String>[] entityList = DustScenario.contextEntity(entity);
                if (entityList[0].size() != 0) ChatState.location = entityList[0];
                if (entityList[1].size() != 0) ChatState.date = entityList[1];
                DustScenario.response(ChatState.location, ChatState.date);
            } else if (ChatState.contextIntent.equals("날씨")) {
                String[][] entity = ModelApi.getEntity("weather", ChatState.tokenizeSpeech);
                List<String>[] entityList = WeatherScenario.contextEntity(entity);
                if (entityList[0].size() != 0) ChatState.location = entityList[0];
                if (entityList[1].size() != 0) ChatState.date = entityList[1];
                WeatherScenario.response(ChatState.location, ChatState.date);
            } else if (ChatState.contextIntent.equals("맛집")) {
                String[][] entity = ModelApi.getEntity("restaurant", ChatState.tokenizeSpeech);
                List<String> entityList = RestaurantScenario.seperateEntity(entity);
                if (entityList.size() != 0) ChatState.location = entityList;
                RestaurantScenario.response(ChatState.location);
            } else {
                ChatState.intent = "날씨";
            }
            return true;
        } else if (ChatState.intent.equals("번역환율문맥")) {
            if (!ChatState.contextIntent.equals("환율")) {
                String[][] entity = ModelApi.getEntity("translate", ChatState.tokenizeSpeech);
                List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                if (entityList[1].size() != 0) ChatState.lang = entityList[1];
                if (ChatState.contextIntent.equals("번역")) {
                    TranslateScenario.response(Arrays.asList(TranslateScenario.getAnswer().split(" ")), ChatState.lang);
                } else if (ChatState.contextIntent.equals("위키")) {
                    TranslateScenario.response(Arrays.asList(WikiScenario.getAnswer().split(" ")), ChatState.lang);
                } else if (ChatState.contextIntent.equals("맛집")) {
                    TranslateScenario.response(Arrays.asList(RestaurantScenario.getAnswer().split(" ")), ChatState.lang);
                } else if (ChatState.contextIntent.equals("날씨")) {
                    TranslateScenario.response(Arrays.asList(WeatherScenario.getAnswer().split(" ")), ChatState.lang);
                } else if (ChatState.contextIntent.equals("뉴스")) {
                    TranslateScenario.response(Arrays.asList(NewsScenario.getAnswer().split(" ")), ChatState.lang);
                } else if (ChatState.contextIntent.equals("먼지")) {
                    TranslateScenario.response(Arrays.asList(DustScenario.getAnswer().split(" ")), ChatState.lang);
                } else if (ChatState.contextIntent.equals("잡담") || ChatState.contextIntent.equals("이슈") || ChatState.contextIntent.equals("명언")) {
                    TranslateScenario.response(Arrays.asList(ChatState.contextGeneratedAnswer.split(" ")), ChatState.lang);
                }
            } else if (ChatState.contextIntent.equals("환율")) {
                String[][] entity = ModelApi.getEntity("exchange", ChatState.tokenizeSpeech);
                List<String> entityList = ExchangeScenario.seperateEntity(entity);
                if (entityList.size() != 0) {
                    ChatState.lang = entityList;
                    ExchangeScenario.response(ChatState.lang);
                } else {
                    ChatState.intent = "번역";
                }
            } else {
                ChatState.intent = "번역";
            }
            return true;


        } else if (ChatState.intent.equals("날씨문맥전환")) {
            if (ChatState.contextIntent.equals("먼지")) {
                WeatherScenario.response(ChatState.location, ChatState.date);
                ChatState.contextIntent = "날씨";
            } else {
                ChatState.intent = "날씨";
            }
            return true;
        } else if (ChatState.intent.equals("먼지문맥전환")) {
            if (ChatState.contextIntent.equals("날씨")) {
                DustScenario.response(ChatState.location, ChatState.date);
                ChatState.contextIntent = "먼지";
            } else {
                ChatState.intent = "먼지";
            }
            return true;
        }
        return false;
    }
}
