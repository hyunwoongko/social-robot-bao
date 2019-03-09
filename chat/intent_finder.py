import pandas as pd
from gensim.models import FastText
from konlpy.tag import Okt


class IntentFinder:
    tokenizer = Okt()

    def __init__(self):
        self.filter_sizes = [2, 3, 4, 2, 3, 4, 2, 3, 4]
        self.num_filters = len(self.filter_sizes)
        self.intent_mapping = {"인사": 0, "잡담": 1, "노래": 2}
        self.sentence = ''
        self.train_data = pd.read_csv('train.csv')
        self.vector_size = 64
        self.model = FastText(size=self.vector_size,
                              window=2,
                              workers=8,
                              min_count=1,
                              sg=1,
                              iter=500)

    def tokenize(self, sentence):
        word_bag = []
        pos = self.tokenizer.pos(sentence, stem=True, norm=True)

        for word, tag in pos:
            if tag == 'Josa' or tag == 'Adverb' or tag == 'Punctuation':
                continue
            else:
                word_bag.append(word)
        result = ' '.join(word_bag)
        return result

    def preprocess(self):
        self.train_data = self.train_data.drop('answer', axis=1)
        self.train_data = self.train_data.drop('emotion', axis=1)
        self.train_data['intent'] = self.train_data['intent'].map(self.intent_mapping)
        for i in self.train_data['question']:
            self.train_data.replace(i, self.tokenize(i), regex=True, inplace=True)
        self.train_data.replace(r'\s', '', regex=True, inplace=True)
        print(self.train_data)
        return self.train_data


finder = IntentFinder()
finder.preprocess()
