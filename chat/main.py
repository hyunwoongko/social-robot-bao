import os

import disintegreator
from emotion_engine import get_emotion
from intent_finder import get_intent

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'

question = input()
disintegreated_data = disintegreator.tokenize(question)

if disintegreated_data in '바오':
    disintegreated_data.replace('바오', '')

print(disintegreated_data)
print('감정 지수 (-2 ~ 2) : ', get_emotion(disintegreated_data))
print('발화 의도: ', get_intent(disintegreated_data, True))
