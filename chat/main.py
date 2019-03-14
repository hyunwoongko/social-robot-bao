import os

from emotion_engine import get_emotion
from entity_recognizer import get_entity
from intent_finder import get_intent
from disintegreator import tokenize
from kor_model.config import config
from kor_model.data_embed_model import data_utils
from kor_model.data_embed_model import word2vec
from kor_model.data_embed_model.data_utils import CoNLLDataset
from kor_model.ner_model.lstmcrf_model import NERModel

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'

question = input()
question = tokenize(question)

if question in '바오':
    question.replace('바오', '')

print(question)
print('감정 지수 (-2 ~ 2) : ', get_emotion(question))
print('발화 의도 : ', get_intent(question, False))
print('개체 분류 : ', get_entity(question, False))
