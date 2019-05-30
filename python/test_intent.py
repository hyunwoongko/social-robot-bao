# Author : Hyunwoong
# When : 5/30/2019
# Homepage : github.com/gusdnd852
from chat.intent.classifier import get_intent

if __name__ == '__main__':
    print("입력하세요 : ")
    while True:
        q = input()
        print(get_intent(q))
