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
    '생일': 5,
    '긍정대답': 6,
    '부정대답': 7,
    '나이': 8,
    '소개': 9,
    '성별': 10,
    '식사': 11,
    '인공지능': 12,
    '다른인공지능': 13,
    '선택': 14,
    '괜찮아': 15,
    '힘듬': 16,
    '취향': 17,
    '영화비목적': 18,
    '노래비목적': 19,
    '되묻기': 20,
    '감사': 21,
    '싸움': 22,
    '장기자랑': 23,
    '비난': 24,
    '화해': 25,
    '고민': 26,
    '아픔': 27,
    '사랑': 28,
    '칭찬': 29,
    '슬픔': 30,
    '짜증': 31,
    '취침': 32,
    '기쁨': 33,
    '피곤': 34,
    '테스트': 35,
    '뭐해': 36,
    '거짓말': 37,
    '양자택일': 38,
    '기상': 39,
    '종교': 40,
    '성장외모': 41,
    '조심': 42,
    '학교': 43,
    '부탁': 44,
    '사과': 45,
    '온습도': 46,
    '공포': 47,
    '장래희망': 48,
    '여행': 49,
    '걱정': 50,
    '축하': 51,
    '주인': 52,
    '바쁨': 53,
    '취미': 54,
    '우정': 55,
    '직장': 56,
    '고향': 57,
    '준비': 58,
    '조언': 59,
    '진짜': 60,
    '여기거기': 61,
    '취소': 62,
    '기다림': 63,
    '웃음': 64,
    '신기': 65,
    '컴백': 66,
    '장난': 67,
    '좋아함': 68,
    '외로움': 69,
    '그리움': 70,
    '미분류': 71,
    '시간': 72,
    '날짜': 73,
    '번역환율문맥': 74,
    '날씨먼지문맥': 75,
    '날씨문맥전환': 76,
    '먼지문맥전환': 77,
    '뉴스': 78,
    '명언': 79,
    '번역': 80,
    '심심': 81,
    '농담': 82,
    '상관없음': 83,
    '배고파': 84,
    '맛집': 85,
    '환율': 86,
    '알람': 87,
    '이슈': 88,
    '노래목적': 89,
    '댄스': 90,
    '동화': 91,
    '포옹': 92,
    '영화목적': 93,
    '먼지': 94,
    '위키': 95,
    '날씨': 96,
    '볼륨업': 97,
    '볼륨다운': 98
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
