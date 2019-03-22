from intent_classifier.intent_classifier import get_intent

while True:
    print("입력하세요.")
    print(get_intent(input(), False))
