package com.welfarerobotics.welfareapplcation.chat_scenario;

import com.welfarerobotics.welfareapplcation.chat_api.IssueApi;
import com.welfarerobotics.welfareapplcation.chat_api.ModelApi;
import com.welfarerobotics.welfareapplcation.chat_api.PreprocessorApi;
import com.welfarerobotics.welfareapplcation.chat_api.WiseApi;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author : Hyunwoong
 * @When : 3/23/2019 10:58 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class ChatApi {

    private static ChatApi api = null;
    private Random random = new Random();

    // 플래그
    private boolean questionMode = false;
    private boolean langMode = false;
    private boolean wordMode = false;

    // 문맥 도구
    private String contextIntent = "잡담";
    private String contextGeneratedAnswer = "";

    // 의도 및 개체명 인식 도구
    private String intent;
    private List<String> location = new ArrayList<>();
    private List<String> date = new ArrayList<>();
    private List<String> lang = new ArrayList<>();
    private List<String> word = new ArrayList<>();

    // 사전 정의 배열
    private String[] borings = {"노래", "동화", "댄스", "농담"};
    private String[] danceMents = {"이 춤은 어떤가요?", "전 이 춤이 좋더군요.", "댄스! 댄스!", "이 춤 정말 신나지 않나요?"};

    private ChatApi() {
    }

    public static ChatApi get() {
        if (api == null) api = new ChatApi();
        return api;
    }

    public void chat(String speech) {
        try {
            if (questionMode && intent.equals("먼지")) {
                if (location.size() == 0) {
                    speech = PreprocessorApi.fix(speech);
                    location.add(speech);
                    if (DustScenario.response(location, date)) {
                        clear();
                        questionMode = false;
                        return;
                    }
                }
            } else if (questionMode && intent.equals("날씨")) {
                if (location.size() == 0) {
                    speech = PreprocessorApi.fix(speech);
                    location.add(speech);
                    if (WeatherScenario.response(location, date)) {
                        clear();
                        questionMode = false;
                        return;
                    }
                }
            } else if (questionMode && intent.equals("환율")) {
                if (location.size() == 0) {
                    speech = PreprocessorApi.fix(speech);
                    location.add(speech);
                    if (ExchangeScenario.response(location)) {
                        clear();
                        questionMode = false;
                        return;
                    }
                }
            } else if (questionMode && intent.equals("맛집")) {
                if (location.size() == 0) {
                    speech = PreprocessorApi.fix(speech);
                    location.add(speech);
                    if (RestaurantScenario.response(location)) {
                        clear();
                        questionMode = false;
                        return;
                    }
                }
            } else if (questionMode && wordMode && intent.equals("번역")) {
                if (word.size() == 0) {
                    speech = PreprocessorApi.fix(speech);
                    word.add(speech);
                    if (TranslateScenario.response(word, lang).equals("no word")) {
                        CssApi.get().play("어느 말을 알려드릴까요?", "jinho");
                    } else {
                        questionMode = false;
                        wordMode = false;
                        return;
                    }
                }
            } else if (questionMode && langMode && intent.equals("번역")) {
                if (lang.size() == 0) {
                    speech = PreprocessorApi.fix(speech);
                    lang.add(speech);
                    if (TranslateScenario.response(word, lang).equals("no lang")) {
                        CssApi.get().play("어느 언어로 알려드릴까요?", "jinho");
                    } else {
                        questionMode = false;
                        langMode = false;
                        return;
                    }
                }
            }// 질문 세션

            if (!questionMode) {
                speech = PreprocessorApi.fix(speech);
                speech = PreprocessorApi.tokenize(speech);
                speech = PreprocessorApi.fix(speech);
                System.out.println(speech);
                //전처리 세션

                intent = ModelApi.getIntent(speech);
                System.out.println(intent);
                // 의도 파악 세션

                if (!intent.contains("문맥"))
                    contextIntent = intent;

                else if (intent.equals("날씨먼지문맥")) {
                    if (contextIntent.equals("먼지")) {
                        String[][] entity = ModelApi.getEntity("dust", speech);
                        List<String>[] entityList = DustScenario.contextEntity(entity);
                        if (entityList[0].size() != 0) location = entityList[0];
                        if (entityList[1].size() != 0) date = entityList[1];
                        DustScenario.response(location, date);
                        return;
                    } else if (contextIntent.equals("날씨")) {
                        String[][] entity = ModelApi.getEntity("weather", speech);
                        List<String>[] entityList = WeatherScenario.contextEntity(entity);
                        if (entityList[0].size() != 0) location = entityList[0];
                        if (entityList[1].size() != 0) date = entityList[1];
                        WeatherScenario.response(location, date);
                        return;
                    } else if (contextIntent.equals("맛집")) {
                        String[][] entity = ModelApi.getEntity("restaurant", speech);
                        List<String> entityList = RestaurantScenario.seperateEntity(entity);
                        if (entityList.size() != 0) location = entityList;
                        RestaurantScenario.response(location);
                        return;
                    } else {
                        intent = "날씨";
                    }
                } else if (intent.equals("번역환율문맥")) {
                    if (contextIntent.equals("번역")) {
                        String[][] entity = ModelApi.getEntity("translate", speech);
                        List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                        if (entityList[1].size() != 0) lang = entityList[1];
                        TranslateScenario.response(Arrays.asList(TranslateScenario.getAnswer().split(" ")), lang);
                        return;
                    } else if (contextIntent.equals("위키")) {
                        String[][] entity = ModelApi.getEntity("translate", speech);
                        List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                        if (entityList[1].size() != 0) lang = entityList[1];
                        TranslateScenario.response(Arrays.asList(WikiScenario.getAnswer().split(" ")), lang);
                        return;
                    } else if (contextIntent.equals("맛집")) {
                        String[][] entity = ModelApi.getEntity("translate", speech);
                        List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                        if (entityList[1].size() != 0) lang = entityList[1];
                        TranslateScenario.response(Arrays.asList(RestaurantScenario.getAnswer().split(" ")), lang);
                        return;
                    } else if (contextIntent.equals("날씨")) {
                        String[][] entity = ModelApi.getEntity("translate", speech);
                        List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                        if (entityList[1].size() != 0) lang = entityList[1];
                        TranslateScenario.response(Arrays.asList(WeatherScenario.getAnswer().split(" ")), lang);
                        return;
                    } else if (contextIntent.equals("뉴스")) {
                        String[][] entity = ModelApi.getEntity("translate", speech);
                        List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                        if (entityList[1].size() != 0) lang = entityList[1];
                        TranslateScenario.response(Arrays.asList(NewsScenario.getAnswer().split(" ")), lang);
                        return;
                    } else if (contextIntent.equals("먼지")) {
                        String[][] entity = ModelApi.getEntity("translate", speech);
                        List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                        if (entityList[1].size() != 0) lang = entityList[1];
                        TranslateScenario.response(Arrays.asList(DustScenario.getAnswer().split(" ")), lang);
                        return;
                    } else if (contextIntent.equals("잡담") || contextIntent.equals("이슈") || contextIntent.equals("명언")) {
                        String[][] entity = ModelApi.getEntity("translate", speech);
                        List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                        if (entityList[1].size() != 0) lang = entityList[1];
                        TranslateScenario.response(Arrays.asList(contextGeneratedAnswer.split(" ")), lang);
                    } else if (contextIntent.equals("환율")) {
                        String[][] entity = ModelApi.getEntity("exchange", speech);
                        List<String> entityList = ExchangeScenario.seperateEntity(entity);
                        if (entityList.size() != 0) {
                            lang = entityList;
                            ExchangeScenario.response(lang);
                        } else {
                            intent = "번역";
                        }
                    } else {
                        intent = "번역";
                    }
                } else if (intent.equals("날씨문맥전환")) {
                    if (contextIntent.equals("먼지")) {
                        WeatherScenario.response(location, date);
                        contextIntent = "날씨";
                    } else {
                        intent = "날씨";
                    }
                } else if (intent.equals("먼지문맥전환")) {
                    if (contextIntent.equals("날씨")) {
                        DustScenario.response(location, date);
                        contextIntent = "먼지";
                    } else {
                        intent = "먼지";
                    }
                }//문맥 판단 세션

                if (intent.equals("먼지")) {
                    clear();
                    String[][] entity = ModelApi.getEntity("dust", speech);
                    List<String>[] entityList = DustScenario.seperateEntity(entity);
                    location = entityList[0];
                    date = entityList[1];
                    if (!DustScenario.response(location, date)) {
                        CssApi.get().play("어느 지역을 알려드릴까요?", "jinho");
                        questionMode = true;
                    }
                } else if (intent.equals("배고파")) {
                    clear();
                    int rand = random.nextInt(3);
                    if (rand == 0) {
                        CssApi.get().play("배가 고프시군요.  제가 맛집을 추천해드릴게요.  어떤 맛집을 알려드릴까요?", "jinho");
                        questionMode = true;
                        intent = "맛집";
                    }
                    if (rand == 1) {
                        CssApi.get().play("배가 많이 고프시군요.  냉장고를 열어서 음식을 꺼내드세요.", "jinho");
                    }
                    if (rand == 2) {
                        CssApi.get().play("저도 배가 고프네요.   꼬르륵..", "jinho");
                    }
                } else if (intent.equals("맛집")) {
                    clear();
                    String[][] entity = ModelApi.getEntity("restaurant", speech);
                    List<String> entityList = RestaurantScenario.seperateEntity(entity);
                    location = entityList;
                    if (!RestaurantScenario.response(location)) {
                        CssApi.get().play("어떤 맛집을 알려드릴까요?", "jinho");
                        questionMode = true;
                    }
                } else if (intent.equals("환율")) {
                    clear();
                    String[][] entity = ModelApi.getEntity("exchange", speech);
                    List<String> entityList = ExchangeScenario.seperateEntity(entity);
                    location = entityList;
                    if (!ExchangeScenario.response(location)) {
                        CssApi.get().play("어느 나라의 환율을 알려드릴까요?", "jinho");
                        questionMode = true;
                    }
                } else if (intent.equals("뉴스")) {
                    clear();
                    String[][] entity = ModelApi.getEntity("news", speech);
                    List<String> entityList = NewsScenario.seperateEntity(entity);
                    word = entityList;
                    NewsScenario.response(word);
                } else if (intent.equals("날씨")) {
                    clear();
                    String[][] entity = ModelApi.getEntity("weather", speech);
                    List<String>[] entityList = WeatherScenario.seperateEntity(entity);
                    location = entityList[0];
                    date = entityList[1];
                    if (!WeatherScenario.response(location, date)) {
                        CssApi.get().play("어느 지역을 알려드릴까요?", "jinho");
                        questionMode = true;
                    }
                } else if (intent.equals("번역")) {
                    clear();
                    String[][] entity = ModelApi.getEntity("translate", speech);
                    List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                    word = entityList[0];
                    lang = entityList[1];
                    String result = TranslateScenario.response(word, lang);
                    if (result.equals("no lang")) {
                        CssApi.get().play("어느 언어로 알려드릴까요?", "jinho");
                        questionMode = true;
                        langMode = true;
                    } else if (result.equals("no word")) {
                        CssApi.get().play("어떤 말을 알려드릴까요?", "jinho");
                        questionMode = true;
                        wordMode = true;
                    } else {
                        if (result.equals("no lang word")) {
                            CssApi.get().play("문장을 정확히 다시 말씀해주세요", "jinho");
                        }
                    }
                } else if (intent.equals("위키")) {
                    clear();
                    String[][] entity = ModelApi.getEntity("wiki", speech);
                    List<String> entityList = WikiScenario.seperateEntity(entity);
                    word = entityList;
                    WikiScenario.response(word);
                } else if (intent.equals("이슈")) {
                    contextGeneratedAnswer = IssueApi.getIssue();
                    CssApi.get().play(PreprocessorApi.fix(contextGeneratedAnswer), "jinho");
                } else if (intent.equals("명언")) {
                    contextGeneratedAnswer = WiseApi.getWise();
                    CssApi.get().play(PreprocessorApi.fix(contextGeneratedAnswer), "jinho");
                } else if (intent.equals("노래")) {
                    String[][] entity = ModelApi.getEntity("song", speech);
                    List<String> song = YoutubeScenario.seperateEntity(entity);
                    String youtubeUrl = YoutubeScenario.response(song);
                    /*TODO 안드로이드 팀 - 유튜브 재생 기능 구현해야함*/
                } else if (intent.equals("알람")) {
                    /*TODO 인공지능 - 알람 시간 및 메모 텍스트 추출 구현해야함*/
                    /*TODO 안드로이드 팀 - 알람 기능 구현해야함*/
                    CssApi.get().play("아직 그 기능은 준비중 입니다.", "jinho");
                } else if (intent.equals("댄스")) {
                    /*TODO 안드로이드 팀 - 블루투스 전송해야함*/
                    /*TODO 라즈베리파이 -  팔 및 목 모터 움직임 구현해야함*/
                    CssApi.get().play(danceMents[random.nextInt(danceMents.length)], "jinho");
                } else if (intent.equals("동화")) {
                    CssApi.get().play("동화를 들려드릴게요.", "jinho");
                    //TODO : 동화 데이터 구해야함
                } else if (intent.equals("농담")) {
                    CssApi.get().play("재밌는 농담을 들려드릴게요", "jinho");
                    //TODO : 농담 데이터 구해야함
                } else if (intent.equals("심심")) {
                    String boring = "심심하시군요. 제가 ";
                    String seed = borings[random.nextInt(borings.length)];
                    if (seed.equals("농담")) {
                        CssApi.get().play(boring + "재밌는 농담을 들려드릴게요", "jinho");
                    } else if (seed.equals("댄스")) {
                        CssApi.get().play(danceMents[random.nextInt(danceMents.length)], "jinho");
                    } else if (seed.equals("노래")) {
                        CssApi.get().play(boring + "듣기 좋은 음악을 추천해드릴게요.", "jinho");
                    } else if (seed.equals("동화")) {
                        CssApi.get().play(boring + "동화를 들려드릴게요.", "jinho");
                    }
                } else if (intent.equals("시간")) {
                    SimpleDateFormat format2 = new SimpleDateFormat("HH시 mm분 ss초");
                    Date time = new Date();
                    String time2 = format2.format(time);
                    contextGeneratedAnswer = "현재 시간은 " + time2 + " 입니다.";
                    CssApi.get().play(contextGeneratedAnswer, "jinho");
                } else if (intent.equals("날짜")) {
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy년 MM월 dd일");
                    Date time = new Date();
                    String time2 = format2.format(time);
                    contextGeneratedAnswer = "오늘 날짜는 " + time2 + " 입니다.";
                    CssApi.get().play(contextGeneratedAnswer, "jinho");
                } else if (intent.equals("잡담")) {
                    contextGeneratedAnswer = PreprocessorApi.fix(ModelApi.generateAnswer(speech));
                    CssApi.get().play(contextGeneratedAnswer, "jinho");
                }
            } // 대화 세션

        } catch (IOException e) {
            CssApi.get().play("죄송해요. 아직 배우는 중이라, 잘 이해하지 못했어요. 다시 말씀해주시겠어요?", "jinho");
        }
    }

    private void clear() {
        location.clear();
        date.clear();
        word.clear();
        lang.clear();
    }
}
