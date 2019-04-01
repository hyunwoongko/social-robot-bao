import pandas as pd
import codecs
from bs4 import BeautifulSoup
import urllib.request
from konlpy.tag import Twitter
import os, re, json, random

from hanspell.spell_checker import fix

import codecs
from bs4 import BeautifulSoup
import urllib.request
from konlpy.tag import Twitter
import os, re, json, random

dict_file = "data_out/markov.json"
dic = {}
twitter = Twitter()


def register_dic(words):
    global dic
    if len(words) == 0: return
    tmp = ["@"]
    for i in words:
        word = i[0]
        if word == "" or word == "\r\n" or word == "\n": continue
        tmp.append(word)
        if len(tmp) < 3: continue
        if len(tmp) > 3: tmp = tmp[1:]
        set_word3(dic, tmp)
        if word == "." or word == "?":
            tmp = ["@"]
            continue
    # 딕셔너리가 변경될 때마다 저장하기
    json.dump(dic, open(dict_file, "w", encoding="utf-8"))


def set_word3(dic, s3):
    w1, w2, w3 = s3
    if not w1 in dic: dic[w1] = {}
    if not w2 in dic[w1]: dic[w1][w2] = {}
    if not w3 in dic[w1][w2]: dic[w1][w2][w3] = 0
    dic[w1][w2][w3] += 1


def make_sentence(head):
    if not head in dic: return ""
    ret = []
    if head != "@": ret.append(head)
    top = dic[head]
    w1 = word_choice(top)
    w2 = word_choice(top[w1])
    ret.append(w1)
    ret.append(w2)
    while True:
        if w1 in dic and w2 in dic[w1]:
            w3 = word_choice(dic[w1][w2])
        else:
            w3 = ""
        ret.append(w3)
        if w3 == "." or w3 == "？ " or w3 == "": break
        w1, w2 = w2, w3
    ret = "".join(ret)
    return fix(ret)


def word_choice(sel):
    keys = sel.keys()
    return random.choice(list(keys))


# 응답
def apply_markov(text, training):
    if training:
        data = 'data_in/ChatBotData.csv'
        data_df = pd.read_csv(data)
        question = data_df['Q'].values
        answer = data_df['A'].values
        data_set = []
        for i in zip(question, answer):
            data_set.append(i[0])
            data_set.append(i[1])

        fp_q = open('data_out/markov.txt', 'a', encoding="utf-8")

        for message in data_set:
            if message:
                fp_q.write(message.replace(':', '').replace(',', '') + '\n')
        for i in data_set:
            words = twitter.pos(i)
            register_dic(words)
            # 사전에 단어가 있다면 그것을 기반으로 문장 만들기
            for word in words:
                face = word[0]
                if face in dic: return make_sentence(face)
        return make_sentence("@")
    else:

        words = twitter.pos(text)
        register_dic(words)
        # 사전에 단어가 있다면 그것을 기반으로 문장 만들기
        for word in words:
            face = word[0]
            if face in dic: return make_sentence(face)
    return make_sentence("@")


if os.path.exists(dict_file):
    dic = json.load(open(dict_file, "r"))


if __name__ == '__main__':
    apply_markov("",True)