from intent_finder import get_intent, intent_mapping, train

train()
print(intent_mapping)
question = input()
print(get_intent(question))
