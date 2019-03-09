import pandas as pd
from gensim.models import Word2Vec

input_data = pd.read_csv('voca.csv').values
word_dict = [sentence[0].split() for sentence in input_data]

model = Word2Vec(word_dict,
                 size=64,
                 window=2,
                 workers=8,
                 min_count=1,
                 sg=1,
                 iter=500)

model.init_sims(replace=True)


def get_similar_words(word):
    similarity = model.wv.most_similar(word)
    similarity_df = pd.DataFrame(similarity, columns=['word', 'similarity'])
    similar_words = [word[0] for word in similarity_df.values]
    return similar_words
