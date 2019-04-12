package com.welfarerobotics.welfareapplcation.api.chat.session;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import com.welfarerobotics.welfareapplcation.api.chat.CssApi;
import com.welfarerobotics.welfareapplcation.api.chat.chatutil.ChatState;
import com.welfarerobotics.welfareapplcation.api.chat.chatutil.Fairytale;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.IssueApi;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.ModelApi;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.PreprocessorApi;
import com.welfarerobotics.welfareapplcation.api.chat.crawler.WiseApi;
import com.welfarerobotics.welfareapplcation.api.chat.scenario.*;
import com.welfarerobotics.welfareapplcation.model.UserModel;
import com.welfarerobotics.welfareapplcation.ui.fairytale.FairytaleActivity;
import com.welfarerobotics.welfareapplcation.ui.youtube.YoutubeActivity;
import com.welfarerobotics.welfareapplcation.util.RandomModule;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author : Hyunwoong
 * @When : 4/3/2019 11:45 AM
 * @Homepage : https://github.com/gusdnd852
 * <p>
 * 목적대화 세션
 */
public final class ProposedTalkingSession {
    private final static String[] borings = {"노래", "동화", "댄스", "농담"};
    private final static String[] danceMents = {
            "이 춤은 어떤가요?",
            "전 이 춤이 좋더군요.",
            "댄스! 댄스!",
            "이 춤 정말 신나지 않나요?"};

    public static boolean proposedTalk(UserModel model, AppCompatActivity activity) throws IOException {
        if (ChatState.intent.equals("먼지")) {
            ChatState.clear();
            String[][] entity = ModelApi.getEntity("dust", ChatState.tokenizeSpeech);
            List<String>[] entityList = DustScenario.seperateEntity(entity);
            ChatState.location = entityList[0];
            ChatState.date = entityList[1];
            if (!DustScenario.response(ChatState.location, ChatState.date)) {
                DustScenario.response(Collections.singletonList(model.getLocation()), ChatState.date);
            }
            return true;
        } else if (ChatState.intent.equals("배고파")) {
            ChatState.clear();
            int rand = RandomModule.random.nextInt(3);
            if (rand == 0) {
                CssApi.get().play("배가 고프시군요.  제가 맛집을 추천해드릴게요.  어떤 맛집을 알려드릴까요?", "jinho");
                ChatState.questionMode = true;
                ChatState.intent = "맛집";
            }
            if (rand == 1) {
                CssApi.get().play("배가 많이 고프시군요.  냉장고를 열어서 음식을 꺼내드세요.", "jinho");
            }
            if (rand == 2) {
                CssApi.get().play("저도 배가 고프네요.   꼬르륵..", "jinho");
            }
            return true;
        } else if (ChatState.intent.equals("맛집")) {
            ChatState.clear();
            String[][] entity = ModelApi.getEntity("restaurant", ChatState.tokenizeSpeech);
            ChatState.location = RestaurantScenario.seperateEntity(entity);
            if (!RestaurantScenario.response(ChatState.location)) {
                CssApi.get().play("어떤 맛집을 알려드릴까요?", "jinho");
                ChatState.questionMode = true;
            }
            return true;
        } else if (ChatState.intent.equals("환율")) {
            ChatState.clear();
            String[][] entity = ModelApi.getEntity("exchange", ChatState.tokenizeSpeech);
            ChatState.location = ExchangeScenario.seperateEntity(entity);
            if (!ExchangeScenario.response(ChatState.location)) {
                CssApi.get().play("어느 나라의 환율을 알려드릴까요?", "jinho");
                ChatState.questionMode = true;
            }
            return true;
        } else if (ChatState.intent.equals("뉴스")) {
            ChatState.clear();
            String[][] entity = ModelApi.getEntity("news", ChatState.tokenizeSpeech);
            ChatState.word = NewsScenario.seperateEntity(entity);
            NewsScenario.response(ChatState.word);
        } else if (ChatState.intent.equals("날씨")) {
            ChatState.clear();
            String[][] entity = ModelApi.getEntity("weather", ChatState.tokenizeSpeech);
            List<String>[] entityList = WeatherScenario.seperateEntity(entity);
            ChatState.location = entityList[0];
            ChatState.date = entityList[1];
            if (!WeatherScenario.response(ChatState.location, ChatState.date)) {
                WeatherScenario.response(Collections.singletonList(model.getLocation()), ChatState.date);
            }
            return true;
        } else if (ChatState.intent.equals("번역")) {
            ChatState.clear();
            String[][] entity = ModelApi.getEntity("translate", ChatState.tokenizeSpeech);
            List<String>[] entityList = TranslateScenario.seperateEntity(entity);
            ChatState.word = entityList[0];
            ChatState.lang = entityList[1];
            String result = TranslateScenario.response(ChatState.word, ChatState.lang);
            if (result.equals("no lang")) {
                CssApi.get().play("어느 언어로 알려드릴까요?", "jinho");
                ChatState.questionMode = true;
                ChatState.langMode = true;
            } else if (result.equals("no word")) {
                CssApi.get().play("어떤 말을 알려드릴까요?", "jinho");
                ChatState.questionMode = true;
                ChatState.wordMode = true;
            } else {
                if (result.equals("no word")) {
                    CssApi.get().play("문장을 정확히 다시 말씀해주세요", "jinho");
                }
            }
            return true;
        } else if (ChatState.intent.equals("위키")) {
            ChatState.clear();
            String[][] entity = ModelApi.getEntity("wiki", ChatState.tokenizeSpeech);
            ChatState.word = WikiScenario.seperateEntity(entity);
            WikiScenario.response(ChatState.word);
            return true;
        } else if (ChatState.intent.equals("이슈")) {
            ChatState.contextGeneratedAnswer = IssueApi.getIssue();
            CssApi.get().play(PreprocessorApi.fix(ChatState.contextGeneratedAnswer), "jinho");
            return true;
        } else if (ChatState.intent.equals("명언")) {
            ChatState.contextGeneratedAnswer = WiseApi.getWise();
            CssApi.get().play(PreprocessorApi.fix(ChatState.contextGeneratedAnswer), "jinho");
            return true;
        } else if (ChatState.intent.equals("노래")) {
            String[][] entity = ModelApi.getEntity("song", ChatState.tokenizeSpeech);
            List<String> song = YoutubeScenario.seperateEntity(entity);
            String youtubeUrl = YoutubeScenario.response(song);
            //Youtube URL에서 ID만 추출
            int UrlIdIndex = youtubeUrl.indexOf("=");
            youtubeUrl = youtubeUrl.substring(UrlIdIndex + 1);
            //YoutubeActivity 실행 및 URL 전달
            Intent youtubeIntent = new Intent(activity.getApplicationContext(), YoutubeActivity.class);
            youtubeIntent.putExtra("url", youtubeUrl);
            activity.startActivity(youtubeIntent);
            return true;
        } else if (ChatState.intent.equals("알람")) {
            /*TODO 인공지능 - 알람 시간 및 메모 텍스트 추출 구현해야함*/
            /*TODO 안드로이드 팀 - 알람 기능 구현해야함*/
            CssApi.get().play("아직 그 기능은 준비중 입니다.", "jinho");
            return true;
        } else if (ChatState.intent.equals("댄스")) {
            /*TODO 안드로이드 팀 - 블루투스 전송해야함*/
            /*TODO 라즈베리파이 -  팔 및 목 모터 움직임 구현해야함*/
            CssApi.get().play(danceMents[RandomModule.random.nextInt(danceMents.length)], "jinho");
            return true;
        } else if (ChatState.intent.equals("동화")) {
            String fairyTale = "";
            Fairytale.get().play();
           // CssApi.get().play("동화를 들려드릴게요. , " + fairyTale, "jinho");
            return true;
            //TODO : 동화 API 연결
        } else if (ChatState.intent.equals("농담")) {
            String joke = "";
            CssApi.get().play("재밌는 농담을 들려드릴게요. , " + joke, "jinho");
            //TODO : 농담 API 연결
        } else if (ChatState.intent.equals("심심")) {
            String boring = "심심하시군요. 제가 ";
            String seed = borings[RandomModule.random.nextInt(borings.length)];
            if (seed.equals("농담")) {
                CssApi.get().play(boring + "재밌는 농담을 들려드릴게요", "jinho");
            } else if (seed.equals("댄스")) {
                CssApi.get().play(danceMents[RandomModule.random.nextInt(danceMents.length)], "jinho");
            } else if (seed.equals("노래")) {
                CssApi.get().play(boring + "듣기 좋은 음악을 추천해드릴게요.", "jinho");
            } else if (seed.equals("동화")) {
                CssApi.get().play(boring + "동화를 들려드릴게요.", "jinho");
            }
            return true;
        } else if (ChatState.intent.equals("시간")) {
            SimpleDateFormat format2 = new SimpleDateFormat("HH시 mm분 ss초");
            Date time = new Date();
            String time2 = format2.format(time);
            ChatState.contextGeneratedAnswer = "현재 시간은 " + time2 + " 입니다.";
            CssApi.get().play(ChatState.contextGeneratedAnswer, "jinho");
            return true;
        } else if (ChatState.intent.equals("날짜")) {
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy년 MM월 dd일");
            Date time = new Date();
            String time2 = format2.format(time);
            ChatState.contextGeneratedAnswer = "오늘 날짜는 " + time2 + " 입니다.";
            CssApi.get().play(ChatState.contextGeneratedAnswer, "jinho");
            return true;
        }
        return false;
    }
}
