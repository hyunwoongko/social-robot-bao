from entity_recognizer.entity_recognizer import get_entity
from intent_classifier.intent_classifier import get_intent


def train_intent():
    get_intent('', True)


def train_entity():
    get_entity('', True)

def train_all():
    get_intent('오늘 날씨', True)
    get_entity('오늘 날씨', True)


if __name__ == '__main__':
    from os import environ;

    environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
    import tensorflow as tf;

    tf.logging.set_verbosity(tf.logging.ERROR)

    # train_intent()
    # train_entity()
    # train_all()
