import pandas as pd


class IntentConfigs:
    encode_length = 16
    filter_sizes = [1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4]
    num_filters = len(filter_sizes)
    learning_step = 50000
    learning_rate = 0.00015
    vector_size = 256

    train_fasttext = False
    tokenizing = True

    def __init__(self):
        self.data = pd.read_csv('../data.csv')
        self.intent_mapping = {}

        idx = -1
        for q, i in self.data.values:
            if i not in self.intent_mapping:
                idx += 1
            self.intent_mapping[i] = idx
        self.label_size = len(self.intent_mapping)


if __name__ == '__main__':
    c = IntentConfigs()
    print("의도 : 번호")
    for k in zip(c.intent_mapping.keys(), c.intent_mapping.values()):
        print(k[0], ' : ', k[1])
