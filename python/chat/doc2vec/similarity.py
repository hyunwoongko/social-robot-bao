# Author : Hyunwoong
# When : 5/13/2019
# Homepage : github.com/gusdnd852
from collections import Counter

import pandas as pd
from gensim.models import Doc2Vec

from chat.doc2vec.configs import Config

conf = Config()
test_data = pd.read_csv(conf.test_path).values


def mode(x):
    counts = Counter(x)
    max_count = max(counts.values())
    return [x_i for x_i, count in counts.items() if count == max_count]


def get_similarity(speech):
    tokens = speech.split(' ')
    model = Doc2Vec.load(conf.modelfile)
    new_vector = model.infer_vector(tokens)
    sims = model.docvecs.most_similar([new_vector], topn=5)
    arr = []

    for word, cos_sim in sims:
        if cos_sim > 0.9:
            print('비목적 스코어 : ', test_data[word][0], ' , ', test_data[word][1], " , ", cos_sim)
            arr.append(test_data[word][1])
    try:
        res = mode(arr)[0]
    except:
        res = '폴백'

    return res
