import random

from konlpy.tag import Okt
import pandas as pd
from hanspell.spell_checker import fix

data = 'data_in/ChatBotData.csv'
data_df = pd.read_csv(data)
question = data_df['Q'].values
answer = data_df['A'].values
data_set = []
for i in zip(question, answer):
    data_set.append(i[0])
    data_set.append(i[1])

fp_q = open('data_out/markov.txt', 'a', encoding="utf-8")

sentence = """안녕하세요. 저는 멍개라고 합니다. 오늘 날씨가 정말로 좋은것 같아요."""

twitter = Okt()
malist = twitter.pos(sentence, norm=True)
words = []

for word in malist:
    if not word[1] in ["Punctuation"]:
        words.append(word[0])
    if word[0] == ' ':
        words.append(word[0])


# 마르코프 체인 딕셔너리 만들기
def make_dic(words):
    tmp = ["@"]
    dic = {}

    for word in words:
        tmp.append(word)

        if len(tmp) < 3: continue
        if len(tmp) > 3: tmp = tmp[1:]

        set_word3(dic, tmp)

        if word == ".":
            tmp = ["@"]
            continue

    return dic


# 딕셔너리에 데이터 등록하기
def set_word3(dic, s3):
    w1, w2, w3 = s3

    if not w1 in dic: dic[w1] = {}
    if not w2 in dic[w1]: dic[w1][w2] = {}
    if not w3 in dic[w1][w2]: dic[w1][w2][w3] = 0

    dic[w1][w2][w3] += 1


print(make_dic(words))


# 문장 만들기
def make_sentence(dic):
    ret = []
    if not '@' in dic: return "no dic"
    top = dic['@']

    w1 = word_choice(top)
    w2 = word_choice(top[w1])

    ret.append(w1)
    ret.append(w2)

    while True:
        w3 = word_choice(dic[w1][w2])
        ret.append(w3)
        if w3 == ".": break
        w1, w2 = w2, w3

    ret = "".join(ret)

    return ret


def word_choice(sel):
    keys = sel.keys()
    return random.choice(list(keys))


dic = make_dic(words)
new_sentence = make_sentence(dic)
print(fix(new_sentence))
