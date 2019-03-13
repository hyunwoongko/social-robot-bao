import pandas as pd

TRAIN_PATH = 'train_intent.csv'


def sentence_data():
    return pd.read_csv(TRAIN_PATH)
