import json
import os
import random

import pandas as pd
from konlpy.tag import Okt

from hanspell.spell_checker import fix


class MarkovEngine:
    def __init__(self, user_id):
        self.user_id = user_id
        self.dic = {}
        self.dic = self.ready()

    def ready(self):
        if os.path.exists("markov_data/" + self.user_id + "/markov.json"):
            dics = json.load(open("markov_data/" + self.user_id + "/markov.json", "r"))
            return dics
        else:
            self.train_markov()
            dics = json.load(open("markov_data/" + self.user_id + "/markov.json", "r"))
            return dics

    def register_dic(self, words):
        if len(words) == 0:
            return
        tmp = ["@"]
        for i in words:
            word = i[0]
            if word == "" or word == "\r\n" or word == "\n":
                continue
            tmp.append(word)
            if len(tmp) < 3:
                continue

            if len(tmp) > 3:
                tmp = tmp[1:]
            self.set_word3(tmp)
            if word == "." or word == "?":
                tmp = ["@"]
                continue
        # 딕셔너리가 변경될 때마다 저장하기
        json.dump(self.dic, open("markov_data/" + self.user_id + "/markov.json", "w", encoding="utf-8"))

    def set_word3(self, s3):
        w1, w2, w3 = s3
        if w1 not in self.dic:
            self.dic[w1] = {}
        if w2 not in self.dic[w1]:
            self.dic[w1][w2] = {}
        if w3 not in self.dic[w1][w2]:
            self.dic[w1][w2][w3] = 0
        self.dic[w1][w2][w3] += 1

    def make_sentence(self, head):
        if head not in self.dic:
            return ""
        ret = []
        if head != "@":
            ret.append(head)
        top = self.dic[head]
        w1 = self.word_choice(top)
        w2 = self.word_choice(top[w1])
        ret.append(w1)
        ret.append(w2)
        while True:
            if w1 in self.dic and w2 in self.dic[w1]:
                w3 = self.word_choice(self.dic[w1][w2])
            else:
                w3 = ""
            ret.append(w3)
            if w3 == "." or w3 == "？ " or w3 == "":
                break
            w1, w2 = w2, w3
        ret = "".join(ret)
        return fix(ret)

    @staticmethod
    def word_choice(sel):
        keys = sel.keys()
        return random.choice(list(keys))

    def train_markov(self):
        data = 'generative_model/data_in/ChatBotData.csv'
        data_df = pd.read_csv(data)
        question = data_df['Q'].values
        answer = data_df['A'].values
        data_set = []
        for i in zip(question, answer):
            data_set.append(i[0])
            data_set.append(i[1])
        if not (os.path.isdir('markov_data/' + self.user_id)):
            os.makedirs(os.path.join('markov_data/' + self.user_id))

        fp_q = open('markov_data/' + self.user_id + '/markov.txt', 'a', encoding="utf-8")

        for message in data_set:
            if message:
                fp_q.write(message.replace(':', '').replace(',', '') + '\n')
        for i in data_set:
            token = Okt()
            words = token.pos(i)
            self.register_dic(words)
            # 사전에 단어가 있다면 그것을 기반으로 문장 만들기
            for word in words:
                face = word[0]
                if face in self.dic:
                    return self.make_sentence(face)
        return self.make_sentence("@")

    # 응답
    def apply_markov(self, text):
        token = Okt()
        words = token.pos(text)
        self.register_dic(words)
        # 사전에 단어가 있다면 그것을 기반으로 문장 만들기
        for word in words:
            face = word[0]
            if face in self.dic:
                return self.make_sentence(face)
        return self.make_sentence("@")
