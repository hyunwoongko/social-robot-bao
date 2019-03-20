import logging
import os

import tensorflow as tf

import chat
from requester import request

logger = logging.getLogger('chardet')
logger.setLevel(logging.CRITICAL)
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
tf.logging.set_verbosity(tf.logging.ERROR)

while True:
    Q = request()
    A = chat.main(Q)
    print(A)
