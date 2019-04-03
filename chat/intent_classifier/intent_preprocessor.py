import pandas as pd
from gensim.models import FastText
from konlpy.tag import Okt

from util.tokenizer import tokenize

data = pd.read_csv('train_intent.csv')
data = data.dropna()
intent_mapping = {
    '인사': 0,
    '작별': 1,
    '호출': 2,
    '가족': 3,
    '불면': 4,
    '상태': 5,
    '생일': 6,
    '긍정대답': 7,
    '부정대답': 8,
    '나이': 9,
    '소개': 10,
    '성별': 11,
    '식사': 12,
    '인공지능': 13,
    '다른인공지능': 14,
    '선택': 15,
    '괜찮아': 16,
    '힘듦': 17,
    '취향': 18,
    '영화비목적': 19,
    '노래목적': 20,
    '되묻기': 21,
    '감사': 22,
    '싸움': 23,
    '장기자랑': 24,
    '비난': 25,
    '화해': 26,
    '고민': 27,
    '아픔': 28,
    '사랑': 29,
    '칭찬': 30,
    '슬픔': 31,
    '짜증': 32,
    '취침': 33,
    '기쁨': 34,
    '피곤': 35,
    '테스트': 36,
    '뭐해': 37,
    '거짓말': 38,
    '네OO': 39,
    '양자택일': 40,
    '기상': 41,
    '종교': 42,
    '성장외모': 43,
    '조심': 44,
    '학교': 45,
    '부탁': 46,
    '사과': 47,
    '온습도': 48,
    '공포': 49,
    '장래희망': 50,
    '여행': 51,
    '걱정': 52,
    '축하': 53,
    '주인': 54,
    '바쁨': 55,
    '취미': 56,
    '우정': 57,
    '놀람': 58,
    '직업': 59,
    '고향': 60,
    '준비': 61,
    '조언': 62,
    '진짜': 63,
    '위치': 64,
    '취소': 65,
    '기다림': 66,
    '쇼핑': 67,
    '웃음': 68,
    '신기': 69,
    '컴백': 70,
    '장난': 71,
    '종아함': 72,
    '외로움': 73,
    '그리움': 74,
    '호칭': 75,
    '연애': 76,
    '도움': 77,
    '격려': 78,
    '희망': 79,
    '놀이': 80,
    '능력': 81,
    '시간': 82,
    '날짜': 83,
    '번역환율문맥': 84,
    '날씨먼지문맥': 85,
    '날씨문맥전환': 86,
    '먼지문맥전환': 87,
    '뉴스': 88,
    '명언': 89,
    '번역': 90,
    '심심': 91,
    '농담': 92,
    '상관없음': 93,
    '배고파': 94,
    '맛집': 95,
    '환율': 96,
    '알람': 97,
    '이슈': 98,
    '노래': 99,
    '댄스': 100,
    '동화': 101,
    '포옹': 102,
    '영화목적': 103,
    '먼지': 104,
    '위키': 105,
    '날씨': 106,
    '볼륨업': 107,
    '볼륨다운': 108
}

vector_size = 256


def intent_size():
    return len(intent_mapping)


def preprocess():
    data['intent'] = data['intent'].map(intent_mapping)

    for i in data['question']:
        data.replace(i, tokenize(i), regex=True, inplace=True)

    encode = []
    decode = []
    for q, i in data.values:
        encode.append(q)
        decode.append(i)

    return {'encode': encode, 'decode': decode}


def train_vector_model():
    mecab = Okt()
    str_buf = preprocess()['encode']
    pos1 = mecab.pos(''.join(str_buf))
    pos2 = ' '.join(list(map(lambda x: '\n' if x[1] in ['Punctuation'] else x[0], pos1))).split('\n')
    morphs = list(map(lambda x: mecab.morphs(x), pos2))
    model = FastText(size=vector_size,
                     window=3,
                     workers=8,
                     min_count=1,
                     sg=1,
                     iter=500)
    model.build_vocab(morphs)
    model.train(morphs, total_examples=model.corpus_count, epochs=model.epochs)
    return model
