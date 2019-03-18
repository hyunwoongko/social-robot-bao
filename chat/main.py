print("깨어나는 중입니다.")
import shutil
import os; os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
import tensorflow as tf;tf.logging.set_verbosity(tf.logging.ERROR)
from generative_model.answer_generator import generate_answer
import scenario.scenario_translation as translation
import scenario.scenario_weather as weather
import scenario.scenario_wiki as wiki
from emotion_engine.emotion_engine import get_emotion
from entity_recognizer.entity_recognizer import get_entity
from hanspell.spell_checker import fix
from intent_classifier.intent_classifier import get_intent
from util.tokenizer import tokenize

if os.path.isdir('data_out'):
    shutil.rmtree('data_out')
shutil.copytree('generative_model/data_out', 'data_out')
# 생성모델 데이터 밖으로 빼내기

while True:
    print("\n> 입력 하세요 : ", end='')
    question = input()
    question = tokenize(question)
    question = fix(question)

    intent = get_intent(question, is_train=False)
    entity = get_entity(question, is_train=False)
    emotion = get_emotion(question)

    print("\n전처리 문장 : ", question)
    print('발화 의도 : ', intent)
    print('개체 분류 : ', entity)
    print('감정 지수 (-2 ~ 2) : ', emotion)

    if intent == '날씨':
        print('> ' + fix(weather.response(entity)))
    elif intent == '번역':
        print('> ' + translation.response(entity))
    elif intent == '위키':
        print('> ' + wiki.response(entity))
    elif intent == '잡담':
        print('> ' + fix(generate_answer(question)))
    else:
        print('> ' + fix(generate_answer(question)))
