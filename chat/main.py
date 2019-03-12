from intent_finder_CNN import get_intent, train
from intent_finder_preprocessor import intent_mapping

train()
print(intent_mapping)
question = input()
print(get_intent(question))
