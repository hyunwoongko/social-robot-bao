from intent_classifier.intent_preprocessor import intent_size



class IntentConfigs:
    encode_length = 12
    label_size = intent_size()
    filter_sizes = [1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4]
    num_filters = len(filter_sizes)
    learning_step = 7000
    learning_rate = 0.00015
