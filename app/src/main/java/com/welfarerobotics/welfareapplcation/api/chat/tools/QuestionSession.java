package com.welfarerobotics.welfareapplcation.api.chat.tools;

/**
 * @Author : Hyunwoong
 * @When : 5/12/2019 9:11 PM
 * @Homepage : https://github.com/gusdnd852
 */
public class QuestionSession {

//    public static void questionProcess(String speech, User model) throws IOException {
//        if (ChatState.intent.equals("환율")) {
//
//            }
//        } else if (ChatState.intent.equals("맛집")) {
//            if (ChatState.location.size() == 0) {
//                speech = PreprocessorApi.fix(speech);
//                String intent = ModelApi.getIntent(speech);
//                if (intent.equals("부정대답")) {
//                    String resultString = "아하 그렇군요.. 알겠습니다.";
//                    resultString = EmotionAdder.applyEmotion(resultString);
//                    CssApi.get().play(resultString, "jinho");
//                    ChatState.clear();
//                    ChatState.questionMode = false;
//                } else if (intent.equals("상관없음")) {
//                    if (RestaurantScenario.response(Collections.singletonList(model.getLocation()))) {
//                        ChatState.clear();
//                        ChatState.questionMode = false;
//                    } else {
//                        ChatState.location.add(speech);
//                        if (RestaurantScenario.response(ChatState.location)) {
//                            ChatState.clear();
//                            ChatState.questionMode = false;
//                        }
//                    }
//                }
//            } else if (ChatState.wordMode && ChatState.intent.equals("번역")) {
//                if (ChatState.word.size() == 0) {
//                    speech = PreprocessorApi.fix(speech);
//                    String intent = ModelApi.getIntent(speech);
//                    if (intent.equals("부정대답")) {
//                        String resultString = "아하 그렇군요.. 알겠습니다.";
//                        resultString = EmotionAdder.applyEmotion(resultString);
//                        CssApi.get().play(resultString, "jinho");
//                        ChatState.clear();
//                        ChatState.questionMode = false;
//                        ChatState.wordMode = false;
//                    } else {
//                        ChatState.word.add(speech);
//                        if (TranslateScenario.response(ChatState.word, ChatState.lang).equals("no word")) {
//                            CssApi.get().play("어느 말을 알려드릴까요?", "jinho");
//                        } else {
//                            ChatState.questionMode = false;
//                            ChatState.wordMode = false;
//                        }
//                    }
//                }
//            } else if (ChatState.langMode && ChatState.intent.equals("번역")) {
//                if (ChatState.lang.size() == 0) {
//                    speech = PreprocessorApi.fix(speech);
//                    if (ModelApi.getIntent(speech).equals("부정대답")) {
//                        String resultString = "아하 그렇군요.. 알겠습니다.";
//                        resultString = EmotionAdder.applyEmotion(resultString);
//                        CssApi.get().play(resultString, "jinho");
//                        ChatState.clear();
//                        ChatState.questionMode = false;
//                        ChatState.langMode = false;
//                    } else {
//                        ChatState.lang.add(speech);
//                        if (TranslateScenario.response(ChatState.word, ChatState.lang).equals("no lang")) {
//                            CssApi.get().play("어느 언어로 알려드릴까요?", "jinho");
//                        } else {
//                            ChatState.questionMode = false;
//                            ChatState.langMode = false;
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
}
