from gensim.models import FastText

from data_loader import voca_data
from tokenizer import tokenize

input_data = voca_data()
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
    similarity = model.wv.most_similar(tokenize(word), topn=topn)
    similar_words = [word[0] for word in similarity]
    return similar_words
