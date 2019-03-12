from gensim.models import FastText
from konlpy.tag import Okt

from data_loader import train_data
from tokenizer import tokenize

data = train_data()
intent_mapping = {
    '인사': 0,
    '잡담': 1,
    '노래': 2
}

vector_size = 64


def intent_size():
    return len(intent_mapping)


def preprocess():
    train_datas = data.drop('answer', axis=1)
    train_datas = train_datas.drop('emotion', axis=1)
    train_datas['intent'] = train_datas['intent'].map(intent_mapping)
    for i in train_datas['question']:
        train_datas.replace(i, tokenize(i), regex=True, inplace=True)

    encode = []
    decode = []
    for q, i in train_datas.values:
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
