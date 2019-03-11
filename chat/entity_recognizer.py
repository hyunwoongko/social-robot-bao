import copy
import pickle

import numpy as np
import torch
from torch.autograd import Variable

from model.CNN_BiLSTM import CNNBiLSTM
from util.data_loader import prepare_sequence, prepare_char_sequence, prepare_lex_sequence
from util.data_utils import load_data_interactive

vocab_path = './data/vocab_ko_NER.pkl'
char_vocab_path = './data/char_vocab_ko_NER.pkl'
pos_vocab_path = './data/pos_vocab_ko_NER.pkl'
lex_dict_path = './data/lex_dict.pkl'
model_load_path = './data/cnn_bilstm_tagger-179-400_f1_0.8739_maxf1_0.8739_100_200_2.pkl'
num_layers = 2
embed_size = 100
hidden_size = 200
gpu_index = 0

predict_NER_dict = {0: '<PAD>',
                    1: '<START>',
                    2: '<STOP>',
                    3: 'B_LC',
                    4: 'B_DT',
                    5: 'B_OG',
                    6: 'B_TI',
                    7: 'B_PS',
                    8: 'I',
                    9: 'O'}

NER_idx_dic = {'<unk>': 0, 'LC': 1, 'DT': 2, 'OG': 3, 'TI': 4, 'PS': 5}


def to_np(x):
    return x.data.cpu().numpy()


def to_var(x, volatile=False):
    if torch.cuda.is_available():
        x = x.cuda(gpu_index)
    return Variable(x)


# apply word2vec
from gensim.models import word2vec

pretrained_word2vec_file = './data/word2vec/ko_word2vec_' + str(embed_size) + '.model'
wv_model_ko = word2vec.Word2Vec.load(pretrained_word2vec_file)
word2vec_matrix = wv_model_ko.wv.syn0


# build vocab
def vocab():
    with open(vocab_path, 'rb') as f:
        return pickle.load(f)


def char_vocab():
    with open(char_vocab_path, 'rb') as f:
        return pickle.load(f)


def pos_vocab():
    with open(pos_vocab_path, 'rb') as f:
        return pickle.load(f)


def lex_dict():
    with open(lex_dict_path, 'rb') as f:
        return pickle.load(f)


# build models
def model():
    bilstm_model = CNNBiLSTM(vocab_size=len(vocab()),
                             char_vocab_size=len(char_vocab()),
                             pos_vocab_size=len(pos_vocab()),
                             lex_ner_size=len(NER_idx_dic),
                             embed_size=embed_size,
                             hidden_size=hidden_size,
                             num_layers=num_layers,
                             word2vec=word2vec_matrix,
                             num_classes=10)

    bilstm_model.load_state_dict(torch.load(model_load_path, map_location=lambda storage, loc: storage))
    return bilstm_model


def preprocessing(x_text_batch, x_pos_batch, x_split_batch):
    x_text_char_item = []
    for x_word in x_text_batch[0]:
        x_char_item = []
        for x_char in x_word:
            x_char_item.append(x_char)
        x_text_char_item.append(x_char_item)
    x_text_char_batch = [x_text_char_item]

    x_idx_item = prepare_sequence(x_text_batch[0], vocab().word2idx)
    x_idx_char_item = prepare_char_sequence(x_text_char_batch[0], char_vocab().word2idx)
    x_pos_item = prepare_sequence(x_pos_batch[0], pos_vocab().word2idx)
    x_lex_item = prepare_lex_sequence(x_text_batch[0], lex_dict())

    x_idx_batch = [x_idx_item]
    x_idx_char_batch = [x_idx_char_item]
    x_pos_batch = [x_pos_item]
    x_lex_batch = [x_lex_item]

    max_word_len = int(
        np.amax([len(word_tokens) for word_tokens in x_idx_batch]))  # ToDo: usually, np.mean can be applied
    batch_size = len(x_idx_batch)
    batch_words_len = [len(word_tokens) for word_tokens in x_idx_batch]
    batch_words_len = np.array(batch_words_len)

    # Padding procedure (word)
    padded_word_tokens_matrix = np.zeros((batch_size, max_word_len), dtype=np.int64)
    for i in range(padded_word_tokens_matrix.shape[0]):
        for j in range(padded_word_tokens_matrix.shape[1]):
            try:
                padded_word_tokens_matrix[i, j] = x_idx_batch[i][j]
            except IndexError:
                pass

    max_char_len = int(np.amax([len(char_tokens) for word_tokens in x_idx_char_batch for char_tokens in word_tokens]))
    if max_char_len < 5:  # size of maximum filter of CNN
        max_char_len = 5

    # Padding procedure (char)
    padded_char_tokens_matrix = np.zeros((batch_size, max_word_len, max_char_len), dtype=np.int64)
    for i in range(padded_char_tokens_matrix.shape[0]):
        for j in range(padded_char_tokens_matrix.shape[1]):
            for k in range(padded_char_tokens_matrix.shape[1]):
                try:
                    padded_char_tokens_matrix[i, j, k] = x_idx_char_batch[i][j][k]
                except IndexError:
                    pass

    # Padding procedure (pos)
    padded_pos_tokens_matrix = np.zeros((batch_size, max_word_len), dtype=np.int64)
    for i in range(padded_pos_tokens_matrix.shape[0]):
        for j in range(padded_pos_tokens_matrix.shape[1]):
            try:
                padded_pos_tokens_matrix[i, j] = x_pos_batch[i][j]
            except IndexError:
                pass

    # Padding procedure (lex)
    padded_lex_tokens_matrix = np.zeros((batch_size, max_word_len, len(NER_idx_dic)))
    for i in range(padded_lex_tokens_matrix.shape[0]):
        for j in range(padded_lex_tokens_matrix.shape[1]):
            for k in range(padded_lex_tokens_matrix.shape[2]):
                try:
                    for x_lex in x_lex_batch[i][j]:
                        k = NER_idx_dic[x_lex]
                        padded_lex_tokens_matrix[i, j, k] = 1
                except IndexError:
                    pass

    x_text_batch = x_text_batch
    x_split_batch = x_split_batch
    padded_word_tokens_matrix = torch.from_numpy(padded_word_tokens_matrix)
    padded_char_tokens_matrix = torch.from_numpy(padded_char_tokens_matrix)
    padded_pos_tokens_matrix = torch.from_numpy(padded_pos_tokens_matrix)
    padded_lex_tokens_matrix = torch.from_numpy(padded_lex_tokens_matrix).float()
    lengths = batch_words_len
    return x_text_batch, x_split_batch, padded_word_tokens_matrix, padded_char_tokens_matrix, padded_pos_tokens_matrix, padded_lex_tokens_matrix, lengths


def parsing_seq2NER(argmax_predictions, x_text_batch):
    predict_NER_list = []
    predict_text_NER_result_batch = copy.deepcopy(x_text_batch[0])  # tuple ([],) -> return first list (batch_size == 1)
    for argmax_prediction_seq in argmax_predictions:
        predict_NER = []
        NER_B_flag = None  # stop B
        prev_NER_token = None
        for i, argmax_prediction in enumerate(argmax_prediction_seq):
            now_NER_token = predict_NER_dict[argmax_prediction.cpu().data.numpy()[0]]
            predict_NER.append(now_NER_token)
            if now_NER_token in ['B_LC', 'B_DT', 'B_OG', 'B_TI', 'B_PS'] and NER_B_flag is None:  # O B_LC
                NER_B_flag = now_NER_token  # start B
                predict_text_NER_result_batch[i] = '<' + predict_text_NER_result_batch[i]
                prev_NER_token = now_NER_token
                if i == len(argmax_prediction_seq) - 1:
                    predict_text_NER_result_batch[i] = predict_text_NER_result_batch[i] + ':' + now_NER_token[-2:] + '>'

            elif now_NER_token in ['B_LC', 'B_DT', 'B_OG', 'B_TI', 'B_PS'] and NER_B_flag is not None:  # O B_LC B_DT
                predict_text_NER_result_batch[i - 1] = predict_text_NER_result_batch[i - 1] + ':' + prev_NER_token[
                                                                                                    -2:] + '>'
                predict_text_NER_result_batch[i] = '<' + predict_text_NER_result_batch[i]
                prev_NER_token = now_NER_token
                if i == len(argmax_prediction_seq) - 1:
                    predict_text_NER_result_batch[i] = predict_text_NER_result_batch[i] + ':' + now_NER_token[-2:] + '>'

            elif now_NER_token in ['I'] and NER_B_flag is not None:
                if i == len(argmax_prediction_seq) - 1:
                    predict_text_NER_result_batch[i] = predict_text_NER_result_batch[i] + ':' + NER_B_flag[-2:] + '>'

            elif now_NER_token in ['O'] and NER_B_flag is not None:  # O B_LC I O
                predict_text_NER_result_batch[i - 1] = predict_text_NER_result_batch[i - 1] + ':' + prev_NER_token[
                                                                                                    -2:] + '>'
                NER_B_flag = None  # stop B
                prev_NER_token = now_NER_token

        predict_NER_list.append(predict_NER)
    return predict_NER_list, predict_text_NER_result_batch


def generate_text_result(text_NER_result_batch, x_split_batch):
    prev_x_split = 0
    text_string = ''
    for i, x_split in enumerate(x_split_batch[0]):
        if prev_x_split != x_split:
            text_string = text_string + ' ' + text_NER_result_batch[i]
            prev_x_split = x_split
        else:
            text_string = text_string + '' + text_NER_result_batch[i]
            prev_x_split = x_split
    return text_string


def entity_recognize(input_str):
    input_str.replace("  ", "")
    input_str = input_str.strip()

    x_text_batch, x_pos_batch, x_split_batch = load_data_interactive(input_str)
    x_text_batch, x_split_batch, padded_word_tokens_matrix, padded_char_tokens_matrix, padded_pos_tokens_matrix, padded_lex_tokens_matrix, lengths = preprocessing(
        x_text_batch, x_pos_batch, x_split_batch)

    # Test
    argmax_labels_list = []
    argmax_predictions_list = []

    padded_word_tokens_matrix = to_var(padded_word_tokens_matrix, volatile=True)
    padded_char_tokens_matrix = to_var(padded_char_tokens_matrix, volatile=True)
    padded_pos_tokens_matrix = to_var(padded_pos_tokens_matrix, volatile=True)
    padded_lex_tokens_matrix = to_var(padded_lex_tokens_matrix, volatile=True)

    predictions = model().sample(padded_word_tokens_matrix, padded_char_tokens_matrix,
                                 padded_pos_tokens_matrix, padded_lex_tokens_matrix, lengths)
    max_predictions, argmax_predictions = predictions.max(2)

    if len(argmax_predictions.size()) != len(
            predictions.size()):
        max_predictions, argmax_predictions = predictions.max(2, keepdim=True)

    argmax_predictions_list.append(argmax_predictions)
    predict_NER_list, predict_text_NER_result_batch = parsing_seq2NER(argmax_predictions, x_text_batch)
    origin_text_string = generate_text_result(x_text_batch[0], x_split_batch)
    predict_NER_text_string = generate_text_result(predict_text_NER_result_batch, x_split_batch)
    return predict_NER_text_string
