package com.welfarerobotics.welfareapplcation.chat_api;

import android.app.Activity;
import android.content.Intent;
import com.welfarerobotics.welfareapplcation.UserModel;
import com.welfarerobotics.welfareapplcation.YoutubeActivity;
import com.welfarerobotics.welfareapplcation.chat_scenario.*;

import java.io.IOException;
import java.lang.ref.WeakReference;
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
    private String emotionType = "";
    private String emotionRate = "";
    private WeakReference<Activity> activityWeakReference;

    public void setEmotionType(String emotionType) {
        this.emotionType = emotionType;
    }

    public void setEmotionRate(String emotionRate) {
        this.emotionRate = emotionRate;
    }

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
    private String name;

    //감정 배열
    private String[] happiness = {
            " , 그런데 오늘따라 기분이 좋아보이세요.",
            " , 그런데 표정을 보니 기분이 좋아보이시네요.",
            " , 근데 오늘 왠지 기분이 좋아보이시는데요.",
            " , 근데 혹시 오늘 좋은일 있으신가요? , 기분이 좋아보여요",
            " , 그런데 지금 기분이 좋아보이시네요.",
            " , 그런데 방금 표정을 보니 오늘 좋은일이 있으신 것 같아요. , 표정이 밝으시네요",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "" // 1/3 확률로 감정 텍스트를 말함 (배열의 1/3 만 채워놓음)
    };

    private String[] surprise = {
            " , 그런데 무슨 일 있으신가요? , 놀란 표정을 지으셔서요.",
            " , 그런데 표정을 보니 놀라보이세요. , 무슨일 있으신가요?",
            " , 근데 지금 표정을 보니 무언가 놀라신것 같아요, , 무슨 일 있으세요?",
            " , 근데 혹시 오늘 무슨일 있으신가요? , 놀라시는 표정을 지으셔서요.",
            " , 그런데 방금 놀란 표정을 지으신것 같네요. 무슨 일 있으신가요?",
            " , 그런데 방금 뭐가 지나갔나요? , 상당히 놀란 표정을 지으셔서요.",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "" // 1/3 확률로 감정 텍스트를 말함 (배열의 1/3 만 채워놓음)
    };

    private ChatApi(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }

    public synchronized static ChatApi get(Activity activity) {
        if (api == null) api = new ChatApi(activity);
        return api;
    }

    public synchronized void chat(String speech, UserModel model) {
        this.name = model.getUserName();
        try {
            if (questionMode && intent.equals("환율")) {
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
                    if (ModelApi.getIntent(speech).equals("상관없음")) {
                        String resultString = "아하 그렇군요.. 알겠습니다.";
                        if (emotionType.equals("happiness"))
                            resultString += happiness[random.nextInt(happiness.length)];
                        else if (emotionType.equals("surprise"))
                            resultString += surprise[random.nextInt(surprise.length)];
                        CssApi.get().play(resultString, "jinho");
                        clear();
                        questionMode = false;
                        return;
                    } else {
                        location.add(speech);
                        if (RestaurantScenario.response(location)) {
                            clear();
                            questionMode = false;
                            return;
                        }
                    }
                }
            } else if (questionMode && wordMode && intent.equals("번역")) {
                if (word.size() == 0) {
                    speech = PreprocessorApi.fix(speech);
                    if (ModelApi.getIntent(speech).equals("상관없음")) {
                        String resultString = "아하 그렇군요.. 알겠습니다.";
                        if (emotionType.equals("happiness"))
                            resultString += happiness[random.nextInt(happiness.length)];
                        else if (emotionType.equals("surprise"))
                            resultString += surprise[random.nextInt(surprise.length)];
                        CssApi.get().play(resultString, "jinho");
                        clear();
                        questionMode = false;
                        wordMode = false;
                        return;
                    } else {
                        word.add(speech);
                        if (TranslateScenario.response(word, lang).equals("no word")) {
                            CssApi.get().play("어느 말을 알려드릴까요?", "jinho");
                        } else {
                            questionMode = false;
                            wordMode = false;
                            return;
                        }
                    }
                }
            } else if (questionMode && langMode && intent.equals("번역")) {
                if (lang.size() == 0) {
                    speech = PreprocessorApi.fix(speech);
                    if (ModelApi.getIntent(speech).equals("상관없음")) {
                        String resultString = "아하 그렇군요.. 알겠습니다.";
                        if (emotionType.equals("happiness"))
                            resultString += happiness[random.nextInt(happiness.length)];
                        else if (emotionType.equals("surprise"))
                            resultString += surprise[random.nextInt(surprise.length)];
                        CssApi.get().play(resultString, "jinho");
                        clear();
                        questionMode = false;
                        langMode = false;
                        return;
                    } else {
                        lang.add(speech);
                        if (TranslateScenario.response(word, lang).equals("no lang")) {
                            CssApi.get().play("어느 언어로 알려드릴까요?", "jinho");
                        } else {
                            questionMode = false;
                            langMode = false;
                            return;
                        }
                    }
                }
            }// 질문 세션

            if (!questionMode) {
                speech = PreprocessorApi.fix(speech);
                String tokenizeSpeech = PreprocessorApi.tokenize(speech);
                tokenizeSpeech = PreprocessorApi.fix(tokenizeSpeech);
                System.out.println(tokenizeSpeech);
                //전처리 세션

                intent = ModelApi.getIntent(tokenizeSpeech);
                System.out.println(intent);
                // 의도 파악 세션

                if (!intent.contains("문맥"))
                    contextIntent = intent;

                else if (intent.equals("날씨먼지문맥")) {
                    if (contextIntent.equals("먼지")) {
                        String[][] entity = ModelApi.getEntity("dust", tokenizeSpeech);
                        List<String>[] entityList = DustScenario.contextEntity(entity);
                        if (entityList[0].size() != 0) location = entityList[0];
                        if (entityList[1].size() != 0) date = entityList[1];
                        DustScenario.response(location, date);
                        return;
                    } else if (contextIntent.equals("날씨")) {
                        String[][] entity = ModelApi.getEntity("weather", tokenizeSpeech);
                        List<String>[] entityList = WeatherScenario.contextEntity(entity);
                        if (entityList[0].size() != 0) location = entityList[0];
                        if (entityList[1].size() != 0) date = entityList[1];
                        WeatherScenario.response(location, date);
                        return;
                    } else if (contextIntent.equals("맛집")) {
                        String[][] entity = ModelApi.getEntity("restaurant", tokenizeSpeech);
                        List<String> entityList = RestaurantScenario.seperateEntity(entity);
                        if (entityList.size() != 0) location = entityList;
                        RestaurantScenario.response(location);
                        return;
                    } else {
                        intent = "날씨";
                    }
                } else if (intent.equals("번역환율문맥")) {
                    if (contextIntent.equals("번역")) {
                        String[][] entity = ModelApi.getEntity("translate", tokenizeSpeech);
                        List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                        if (entityList[1].size() != 0) lang = entityList[1];
                        TranslateScenario.response(Arrays.asList(TranslateScenario.getAnswer().split(" ")), lang);
                        return;
                    } else if (contextIntent.equals("위키")) {
                        String[][] entity = ModelApi.getEntity("translate", tokenizeSpeech);
                        List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                        if (entityList[1].size() != 0) lang = entityList[1];
                        TranslateScenario.response(Arrays.asList(WikiScenario.getAnswer().split(" ")), lang);
                        return;
                    } else if (contextIntent.equals("맛집")) {
                        String[][] entity = ModelApi.getEntity("translate", tokenizeSpeech);
                        List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                        if (entityList[1].size() != 0) lang = entityList[1];
                        TranslateScenario.response(Arrays.asList(RestaurantScenario.getAnswer().split(" ")), lang);
                        return;
                    } else if (contextIntent.equals("날씨")) {
                        String[][] entity = ModelApi.getEntity("translate", tokenizeSpeech);
                        List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                        if (entityList[1].size() != 0) lang = entityList[1];
                        TranslateScenario.response(Arrays.asList(WeatherScenario.getAnswer().split(" ")), lang);
                        return;
                    } else if (contextIntent.equals("뉴스")) {
                        String[][] entity = ModelApi.getEntity("translate", tokenizeSpeech);
                        List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                        if (entityList[1].size() != 0) lang = entityList[1];
                        TranslateScenario.response(Arrays.asList(NewsScenario.getAnswer().split(" ")), lang);
                        return;
                    } else if (contextIntent.equals("먼지")) {
                        String[][] entity = ModelApi.getEntity("translate", tokenizeSpeech);
                        List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                        if (entityList[1].size() != 0) lang = entityList[1];
                        TranslateScenario.response(Arrays.asList(DustScenario.getAnswer().split(" ")), lang);
                        return;
                    } else if (contextIntent.equals("잡담") || contextIntent.equals("이슈") || contextIntent.equals("명언")) {
                        String[][] entity = ModelApi.getEntity("translate", tokenizeSpeech);
                        List<String>[] entityList = TranslateScenario.seperateEntity(entity);
                        if (entityList[1].size() != 0) lang = entityList[1];
                        TranslateScenario.response(Arrays.asList(contextGeneratedAnswer.split(" ")), lang);
                    } else if (contextIntent.equals("환율")) {
                        String[][] entity = ModelApi.getEntity("exchange", tokenizeSpeech);
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
                    String[][] entity = ModelApi.getEntity("dust", tokenizeSpeech);
                    List<String>[] entityList = DustScenario.seperateEntity(entity);
                    location = entityList[0];
                    date = entityList[1];
                    if (!DustScenario.response(location, date)) {
                        DustScenario.response(Collections.singletonList(model.getLocation()), date);
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
                    String[][] entity = ModelApi.getEntity("restaurant", tokenizeSpeech);
                    List<String> entityList = RestaurantScenario.seperateEntity(entity);
                    location = entityList;
                    if (!RestaurantScenario.response(location)) {
                        CssApi.get().play("어떤 맛집을 알려드릴까요?", "jinho");
                        questionMode = true;
                    }
                } else if (intent.equals("환율")) {
                    clear();
                    String[][] entity = ModelApi.getEntity("exchange", tokenizeSpeech);
                    List<String> entityList = ExchangeScenario.seperateEntity(entity);
                    location = entityList;
                    if (!ExchangeScenario.response(location)) {
                        CssApi.get().play("어느 나라의 환율을 알려드릴까요?", "jinho");
                        questionMode = true;
                    }
                } else if (intent.equals("뉴스")) {
                    clear();
                    String[][] entity = ModelApi.getEntity("news", tokenizeSpeech);
                    List<String> entityList = NewsScenario.seperateEntity(entity);
                    word = entityList;
                    NewsScenario.response(word);
                } else if (intent.equals("날씨")) {
                    clear();
                    String[][] entity = ModelApi.getEntity("weather", tokenizeSpeech);
                    List<String>[] entityList = WeatherScenario.seperateEntity(entity);
                    location = entityList[0];
                    date = entityList[1];
                    if (!WeatherScenario.response(location, date)) {
                        WeatherScenario.response(Collections.singletonList(model.getLocation()), date);
                    }
                } else if (intent.equals("번역")) {
                    clear();
                    String[][] entity = ModelApi.getEntity("translate", tokenizeSpeech);
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
                    String[][] entity = ModelApi.getEntity("wiki", tokenizeSpeech);
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
                    String[][] entity = ModelApi.getEntity("song", tokenizeSpeech);
                    List<String> song = YoutubeScenario.seperateEntity(entity);
                    String youtubeUrl = YoutubeScenario.response(song);
                    System.out.println(youtubeUrl);
                    Intent youtubeIntent = new Intent(
                            activityWeakReference.get().getApplicationContext(), YoutubeActivity.class);
                    youtubeIntent.putExtra("url", youtubeUrl);
                    activityWeakReference.get().startActivity(youtubeIntent);
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
                } else if (contextIntent.equals("양자택일")) {
                    if (intent.equals("긍정대답")) {
                        contextGeneratedAnswer = PreprocessorApi.fix(ModelApi.generateAnswer(model.getUserId(), tokenizeSpeech));
                        if (contextGeneratedAnswer.contains("당신")) {
                            contextGeneratedAnswer = contextGeneratedAnswer.replace("당신", model.getUserName() + " 님");
                        }
                        CssApi.get().play(contextGeneratedAnswer + " , 그래서 결정은 하셨나요?", "jinho");
                    } else if (intent.equals("부정대답")) {
                        contextGeneratedAnswer = PreprocessorApi.fix(ModelApi.generateAnswer(model.getUserId(), tokenizeSpeech));
                        if (contextGeneratedAnswer.contains("당신")) {
                            contextGeneratedAnswer = contextGeneratedAnswer.replace("당신", model.getUserName() + " 님");
                        }
                        CssApi.get().play(contextGeneratedAnswer + " , 그래서 아직 결정 못하셨나요?", "jinho");
                    } else {
                        contextGeneratedAnswer = PreprocessorApi.fix(ModelApi.generateAnswer(model.getUserId(), tokenizeSpeech));
                        if (contextGeneratedAnswer.contains("당신")) {
                            contextGeneratedAnswer = contextGeneratedAnswer.replace("당신", model.getUserName() + " 님");
                        }
                        if (emotionType.equals("happiness"))
                            contextGeneratedAnswer += happiness[random.nextInt(happiness.length)];
                        else if (emotionType.equals("surprise"))
                            contextGeneratedAnswer += surprise[random.nextInt(surprise.length)];
                        CssApi.get().play(contextGeneratedAnswer, "jinho");
                    }
                } else {
                    contextGeneratedAnswer = PreprocessorApi.fix(ModelApi.generateAnswer(model.getUserId(), tokenizeSpeech));
                    if (contextGeneratedAnswer.contains("당신")) {
                        contextGeneratedAnswer = contextGeneratedAnswer.replace("당신", model.getUserName() + " 님");
                    }
                    if (emotionType.equals("happiness"))
                        contextGeneratedAnswer += happiness[random.nextInt(happiness.length)];
                    else if (emotionType.equals("surprise"))
                        contextGeneratedAnswer += surprise[random.nextInt(surprise.length)];
                    CssApi.get().play(contextGeneratedAnswer, "jinho");
                }
            } // 대화 세션

        } catch (IOException e) {
            e.printStackTrace();
            CssApi.get().play("죄송해요. 아직 배우는 중이라, 잘 이해하지 못했어요. 다시 말씀해주시겠어요?", "jinho");
        }
    }

    private void clear() {
        location.clear();
        date.clear();
        word.clear();
        lang.clear();
        activityWeakReference = null;
    }
}
