# Author : Hyunwoong
# When : 5/24/2019
# Homepage : github.com/gusdnd852

import os

import pandas as pd


def make_json_file_for_firebase():
    root = './data/'
    data_path = ['동사', '명사', 'etc']
    for path in data_path:
        specific_paths = os.listdir(root + '/' + path)
        for specific_path in specific_paths:
            file_names = os.listdir(root + '/' + path + '/' + specific_path)
            for i, filename in enumerate(file_names):
                try:
                    if filename.split('.')[1] == 'json':
                        print('}, \n\"', filename.split('.')[0], '\" : ', sep='')
                        file = pd.read_csv(root + '/' + path + '/' + specific_path + '/' + filename,
                                           names=[''], header=None, index_col=None, sep='}').values
                        for j, line in enumerate(file):
                            line[0].replace('nan', '}')
                            print(line[0], end='')

                            if ']' in line[0]:
                                print('},')
                except:
                    pass
    print('}')

if __name__ == '__main__':
    make_json_file_for_firebase()
