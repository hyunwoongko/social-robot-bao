# Author : Hyunwoong
# When : 5/10/2019
# Homepage : github.com/gusdnd852
import os

import pandas as pd


def make_doc2vec_data():
    result = pd.DataFrame(columns=['question'])
    root = './data/'
    data_path = ['동사', '명사', 'etc']
    for path in data_path:
        specific_paths = os.listdir(root + '/' + path)
        for specific_path in specific_paths:
            file_names = os.listdir(root + '/' + path + '/' + specific_path)
            for filename in file_names:
                try:
                    if filename.split('.')[1] == 'csv':
                        file = pd.read_csv(root + '/' + path + '/' + specific_path + '/' + filename,
                                           names=['question', 'intent'])
                        result = pd.concat([result, file], sort=True)
                except:
                    pass
    result = pd.DataFrame(result, columns=['question'])
    result.to_csv('./chat/doc2vec/train_doc2vec.csv', index=None, header=None)

    result = pd.DataFrame(columns=['question', 'intent'])
    root = './data/'
    data_path = ['동사', '명사', 'etc']
    for path in data_path:
        specific_paths = os.listdir(root + '/' + path)
        for specific_path in specific_paths:
            file_names = os.listdir(root + '/' + path + '/' + specific_path)
            for filename in file_names:
                try:
                    if filename.split('.')[1] == 'csv':
                        file = pd.read_csv(root + '/' + path + '/' + specific_path + '/' + filename,
                                           names=['question', 'intent'])
                        result = pd.concat([result, file], sort=True)
                except:
                    pass
    result = pd.DataFrame(result, columns=['question', 'intent'])
    result.to_csv('./chat/doc2vec/test_doc2vec.csv', index=None)
    data = result
    intent_mapping = {}

    for q, i in data.values:
        intent_mapping[i] = 0

    for q, i in data.values:
        intent_mapping[i] += 1

    for i, x in enumerate(intent_mapping.items()):
        print(i, ' : ', x)
