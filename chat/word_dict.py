import pandas as pd
from gensim.models import FastText

input_data = pd.read_csv('voca.csv').values
word_dict = [sentence[0].split() for sentence in input_data]

model = FastText(word_dict,
                 size=32,
                 window=3,
                 workers=8,
                 min_count=1,
                 sg=1,
                 iter=250)

model.init_sims(replace=True)


def get_similar_words(word):
    similarity = model.wv.most_similar(word, topn=5)
    similar_words = [word[0] for word in similarity]
    return similar_words
