import os
import shutil

import scenario.scenario_dance as dance
import scenario.scenario_dust as dust
import scenario.scenario_song as song
import scenario.scenario_translation as translation
import scenario.scenario_weather as weather
import scenario.scenario_wiki as wiki
import scenario.scenario_wise as wise
from api.api_issue import get_issue
from entity_recognizer import get_entity
from generative_model.answer_generator import generate_answer
from hanspell.spell_checker import fix
from intent_classifier.intent_classifier import get_intent
from util.tokenizer import tokenize

if os.path.isdir('data_out'):
    shutil.rmtree('data_out')
shutil.copytree('generative_model/data_out', 'data_out')


# 생성모델 데이터 밖으로 빼내기

def main(question):
    question = tokenize(question)
    question = fix(question)
    intent = get_intent(question, is_train=False)
    print("발화 의도 : " + intent)
    if intent == '날씨':
        entity = get_entity(question, is_train=False)
        print(entity)
        return fix(weather.response(entity))
    elif intent == '번역':
        entity = get_entity(question, is_train=False)
        print(entity)
        return '> ' + translation.response(entity)
    elif intent == '위키':
        entity = get_entity(question, is_train=False)
        print(entity)
        return '> ' + wiki.response(entity)
    elif intent == '먼지':
        entity = get_entity(question, is_train=False)
        print(entity)
        return '> ' + dust.response(entity)
    elif intent == '노래':
        entity = get_entity(question, is_train=False)
        print(entity)
        return '>' + song.response(entity)
    elif intent == '이슈':
        return '>' + get_issue()
    elif intent == '댄스':
        return '>' + dance.response()
    elif intent == '명언':
        return '>' + wise.response()
    elif intent == '알람':
        return '> 알람은 준비중 입니다'
    elif intent == '잡담':
        return '> ' + fix(generate_answer(question))
    else:
        return '> ' + fix(generate_answer(question))
