from kor_model.data_crawler import mecab
from kor_model.data_embed_model import build_data
from kor_model.config import config
from kor_model.ner_model.lstmcrf_model import NERModel
from kor_model.general_utils import get_logger
from kor_model.data_embed_model import data_utils
from kor_model.data_embed_model.data_utils import CoNLLDataset
from kor_model.data_embed_model import word2vec
from kor_model.data_embed_model import data_utils
import os

# (2) Word2Vec 를 이용하여 단어 단위로 Embedding Vector 를 구성
embed_model = word2vec.train_w2v(config)


# (3) Generators Class 생성 Iterator
dev   = CoNLLDataset(config.dev_filename, max_iter=config.max_iter)
test  = CoNLLDataset(config.test_filename, max_iter=config.max_iter)
train = CoNLLDataset(config.train_filename, max_iter=config.max_iter)

# (4) Data Set 에서 Word 와 Tag Distinct Value 를 추출
vocab_words, vocab_tags = data_utils.get_vocabs([train, dev, test])

# (5) Word Embedding 에 등록된 Dict 와 훈련 Data Set 에 공통으로 있는 것만 사용
vocab = vocab_words & set(embed_model.wv.index2word)
vocab.add(data_utils.UNK)

# (6) 훈련 데이터에서 Char Dict 추출
vocab_chars = data_utils.get_char_vocab(train)

# (7) 모든 Dict 리스트 및 Vector 파일을 저장함
# Char, Word, Tag 3가지에 대하여 Vector 변환을 위한 데이터
data_utils.write_char_embedding(vocab_chars, config.charembed_filename)
data_utils.write_vocab(vocab_chars, config.chars_filename)
data_utils.write_vocab(vocab, config.words_filename)
data_utils.write_vocab(vocab_tags, config.tags_filename)
data_utils.export_trimmed_glove_vectors(vocab, embed_model, config.trimmed_filename)

# (8) 위에서 저장한 파일들을 로드
embeddings = data_utils.get_trimmed_glove_vectors(config.trimmed_filename)
char_embedding = data_utils.get_trimmed_glove_vectors(config.charembed_filename)
vocab_words = data_utils.load_vocab(config.words_filename)
vocab_tags = data_utils.load_vocab(config.tags_filename)
vocab_chars = data_utils.load_vocab(config.chars_filename)

# (9) 데이터 필터링 작업을 위한 Method
processing_word = data_utils.get_processing_word(vocab_words,
                                                 vocab_chars,
                                                 lowercase=config.lowercase,
                                                 chars=config.chars)
processing_tag = data_utils.get_processing_word(vocab_tags,
                                                lowercase=False)

# 최종적으로 훈련에 사용하는 데이터 객체 (Iterator)
dev = CoNLLDataset(config.dev_filename, processing_word, processing_tag, config.max_iter)
test = CoNLLDataset(config.test_filename, processing_word, processing_tag, config.max_iter)
train = CoNLLDataset(config.train_filename, processing_word, processing_tag, config.max_iter)

# build model
model = NERModel(config, embeddings, ntags=len(vocab_tags),nchars=len(vocab_chars), logger=None, char_embed=char_embedding)
model.build()
model.train(train, dev, vocab_tags)
model.evaluate(test, vocab_tags)

model.predict(vocab_tags, processing_word, "오늘 7시에 노래 틀어줄래")
model.predict(vocab_tags, processing_word, "내일 12시에 알람 맞춰주라")
model.predict(vocab_tags, processing_word, "이따 동요 틀어줘")
model.predict(vocab_tags, processing_word, "모레 알람 7시에 맞춰줄래")
model.predict(vocab_tags, processing_word, "이따 7시에 노래 틀어줄래")

