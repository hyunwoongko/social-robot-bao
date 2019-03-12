import intent_finder_CNN as intent
from intent_finder_preprocessor import intent_mapping

print("입력하세용")
data = input()
print(intent.get_intent(data, False))
print(intent_mapping)
