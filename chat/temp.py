from entity_recognizer.dust.entity_recognizer import get_dust_entity
from intent_classifier.intent_classifier import get_intent

while True:
    print("입력하세요.")
    print(get_dust_entity(input(), False))
