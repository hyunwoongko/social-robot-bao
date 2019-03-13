import os

import disintegreator
from emotion_engine import get_emotion
from intent_finder import get_intent

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'

question = input()
disintegreated_data = disintegreator.tokenize(question)
print(disintegreated_data)
print(get_emotion(disintegreated_data))
print(get_intent(disintegreated_data, True))
