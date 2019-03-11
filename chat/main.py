from util.data_utils import Vocabulary # 이거 무조건 있어야함.
from entity_recognizer import entity_recognize
from intent_finder import get_intent, intent_mapping, train

train()
print(intent_mapping)
question = input()
print(get_intent(question))
print(entity_recognize(question))

