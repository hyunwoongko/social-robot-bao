from intent_finder import get_intent, intent_mapping, train

train()
print(intent_mapping)
print(get_intent(input()))
