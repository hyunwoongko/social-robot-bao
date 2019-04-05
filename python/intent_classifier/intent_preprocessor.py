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
    '영화': 19,
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
    '네OO': 38,
    '양자택일': 39,
    '기상': 40,
    '종교': 41,
    '성장외모': 42,
    '조심': 43,
    '학교': 44,
    '부탁': 45,
    '사과': 46,
    '온습도': 47,
    '공포': 48,
    '장래희망': 49,
    '여행': 50,
    '걱정': 51,
    '축하': 52,
    '주인': 53,
    '바쁨': 54,
    '취미': 55,
    '우정': 56,
    '놀람': 57,
    '직업': 58,
    '고향': 59,
    '준비': 60,
    '조언': 61,
    '진짜': 62,
    '위치': 63,
    '취소': 64,
    '기다림': 65,
    '쇼핑': 66,
    '웃음': 67,
    '신기': 68,
    '컴백': 69,
    '장난': 70,
    '종아함': 71,
    '외로움': 72,
    '그리움': 73,
    '호칭': 74,
    '연애': 75,
    '도움': 76,
    '격려': 77,
    '희망': 78,
    '놀이': 79,
    '능력': 80,
    '시간': 81,
    '날짜': 82,
    '번역환율문맥': 83,
    '날씨먼지문맥': 84,
    '날씨문맥전환': 85,
    '먼지문맥전환': 86,
    '뉴스': 87,
    '명언': 88,
    '번역': 89,
    '심심': 90,
    '농담': 91,
    '상관없음': 92,
    '배고파': 93,
    '맛집': 94,
    '환율': 95,
    '알람': 96,
    '이슈': 97,
    '노래': 98,
    '댄스': 99,
    '동화': 100,
    '포옹': 101,
    '영화목적': 102,
    '먼지': 103,
    '위키': 104,
    '날씨': 105,
    '볼륨업': 106,
    '볼륨다운': 107
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
