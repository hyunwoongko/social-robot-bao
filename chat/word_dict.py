import pandas as pd
from gensim.models import FastText

input_data = pd.read_csv('voca.csv').values
word_dict = [sentence[0].split() for sentence in input_data]

model = FastText(word_dict,
                 size=64,
                 window=2,
                 workers=8,
                 min_count=1,
                 sg=1,
                 iter=300)

model.init_sims(replace=True)


def get_similar_words(word, topn=5):
    similarity = model.wv.most_similar(word, topn=topn)
    similar_words = [word[0] for word in similarity]
    return similar_words
