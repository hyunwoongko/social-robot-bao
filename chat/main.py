import tensorflow as tf

from emotion_engine.emotion_engine import get_emotion
from entity_recognizer import get_entity
from hanspell.spell_checker import fix
from intent_classifier.intent_classifier import get_intent
from util.tokenizer import tokenize

tf.logging.set_verbosity(tf.logging.ERROR)

question = input()
question = fix(question)
question = tokenize(question)

print(question)
print('발화 의도 : ', get_intent(question, False))
print('개체 분류 : ', get_entity(question, False))
print('감정 지수 (-2 ~ 2) : ', get_emotion(question))
