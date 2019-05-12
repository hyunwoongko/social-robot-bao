import pandas as pd


class IntentConfigs:
    encode_length = 16
    filter_sizes = [3, 4, 5, 6, 3, 4, 5, 6, 3, 4, 5, 6, 3, 4, 5, 6]
    num_filters = len(filter_sizes)
    learning_step = 20000
    learning_rate = 0.00005
    vector_size = 384
    fallback_score = 2.5
    train_fasttext = False
    tokenizing = True

    def __init__(self):
        self.data = pd.read_csv('./chat/intent/train_intent.csv')
        self.intent_mapping = {}

        idx = -1
        for q, i in self.data.values:
            if i not in self.intent_mapping:
                idx += 1
            self.intent_mapping[i] = idx
        self.label_size = len(self.intent_mapping)
