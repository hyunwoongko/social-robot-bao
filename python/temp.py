# Author : Hyunwoong
# When : 5/9/2019
# Homepage : github.com/gusdnd852
from chat.hanspell.spell_checker import fix
from chat.intent.classifier import get_intent
from chat.intent.train import train

if __name__ == '__main__':
    train()
    # print("입력")
    # while True:
    #     res = get_intent(fix(input()))
    #     print(res)
