# Author : Hyunwoong
# When : 5/9/2019
# Homepage : github.com/gusdnd852
from chat.hanspell.spell_checker import fix
from chat.intent.classifier import get_intent
# from chat.intent.train import train
from chat.util.tokenizer import tokenize

if __name__ == '__main__':
    # train()
    print("입력")
    while True:
        sp = input()
        sp = fix(sp)
        sp = tokenize(sp)
        sp = fix(sp)
        res = get_intent(sp)
        print(res)
