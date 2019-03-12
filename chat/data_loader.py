import pandas as pd

TRAIN_PATH = 'train.csv'
VOCA_PATH = 'voca.csv'


def train_data():
    return pd.read_csv(TRAIN_PATH)


def voca_data():
    return pd.read_csv(VOCA_PATH)
