from gensim.models import FastText
from konlpy.tag import Okt

from data_loader import sentence_data
from disintegreator import tokenize

data = sentence_data()
intent_mapping = {
    '인사': 0,
    '날씨': 1,
    '위키': 2,
    '구글': 3,
    '먼지': 4,
    '배고파': 5,
    '고민': 6,
    '댄스': 7,
    '동화': 8,
    '노래': 9,
    '이슈': 10,
    '알람': 11
}

vector_size = 64


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
                     window=2,
                     workers=8,
                     min_count=1,
                     sg=1,
                     iter=300)
    model.build_vocab(morphs)
    model.train(morphs, total_examples=model.corpus_count, epochs=model.epochs)
    return model
