from hanspell.spell_checker import fix
from intent_classifier.intent_classifier import get_intent
from util.tokenizer import tokenize

while True:
    print("입력하세요 : ", end='')
    q = input()
    q = tokenize(q)
    q = fix(q)

    print(get_intent(q, False))
