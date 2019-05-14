# Author : Hyunwoong
# When : 5/12/2019
# Homepage : github.com/gusdnd852
import logging
import os

import tensorflow as tf

from chat.doc2vec.similarity import get_similarity
from chat.entity.news.entity_recognizer import get_news_entity
from chat.entity.restaurant.entity_recognizer import get_restaurant_entity
from chat.entity.song.entity_recognizer import get_song_entity
from chat.entity.translate.entity_recognizer import get_translate_entity
from chat.entity.weather.entity_recognizer import get_weather_entity
from chat.entity.wiki.entity_recognizer import get_wiki_entity
from chat.intent.classifier import get_intent
from chat.util.hanspell.spell_checker import fix
from chat.util.tokenizer import tokenize

logger = logging.getLogger('chardet')
logger.setLevel(logging.CRITICAL)
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
tf.logging.set_verbosity(tf.logging.ERROR)


def pre_process(speech):
    return fix(tokenize(fix(speech)))


def get_entity(intent, speech):
    entity = None
    if intent == '뉴스':
        entity = get_news_entity(speech, is_train=False)
    elif intent == '맛집':
        entity = get_restaurant_entity(speech, is_train=False)
    elif intent == '음악':
        entity = get_song_entity(speech, is_train=False)
    elif intent == '번역':
        entity = get_translate_entity(speech, is_train=False)
    elif intent == '날씨' or intent == '먼지':
        entity = get_weather_entity(speech, is_train=False)
    elif intent == '인물' or intent == '위키':
        entity = get_wiki_entity(speech, is_train=False)
    return entity


if __name__ == '__main__':
    print("깨어났습니다.")
    while True:
        speech = input()
        speech = pre_process(speech)
        print("전처리 : ", speech)

        intent = get_intent(speech)
        entity = None
        print('의도 : ' + intent)
        if intent != '폴백':
            entity = get_entity(intent, speech)
        else:
            intent = get_similarity(speech)
        print('의도 : ' + intent)

        if entity is not None:
            print('개체 : ' + str(entity))
