# Author : Hyunwoong
# When : 5/13/2019
# Homepage : github.com/gusdnd852
import os

from gensim.models import doc2vec

from chat.doc2vec.configs import Config


def train_doc2vec():
    conf = Config()
    sentences = doc2vec.TaggedLineDocument(conf.train_path)
    # build voca
    model = doc2vec.Doc2Vec(min_count=conf.word_min_count,
                            vector_size=conf.vector_size,
                            alpha=conf.learning_rate,
                            negative=conf.negative_size,
                            epochs=conf.train_epoch,
                            window_size=conf.window_size,
                            min_alpha=conf.learning_rate,
                            seed=1234,
                            workers=conf.worker_count)

    model.build_vocab(sentences)

    # Train document vectors
    model.train(sentences, epochs=model.iter,
                total_examples=model.corpus_count)

    # To save
    if not os.path.isdir(conf.model_path):
        os.mkdir(conf.model_path)
    model.save(conf.modelfile)
