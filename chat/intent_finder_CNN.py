import os

import numpy as np
import pandas as pd
import tensorflow as tf
from konlpy.tag import Okt

from intent_finder_preprocessor import intent_size, vector_size, preprocess, train_vector_model
from util_tokenizer import tokenize

# 파라미터 세팅
train_data_list = preprocess()
encode_length = 4
label_size = intent_size()
filter_sizes = [2, 3, 4, 2, 3, 4, 2, 3, 4]
num_filters = len(filter_sizes)
learning_step = 2500
learning_rate = 0.0005
model = train_vector_model()


def load_csv(data_path):
    df_csv_read = pd.DataFrame(data_path)
    return df_csv_read


def embed(data):
    mecab = Okt()
    inputs = []
    labels = []
    for encode_raw in data['encode']:
        encode_raw = mecab.morphs(encode_raw)
        encode_raw = list(map(lambda x: encode_raw[x] if x < len(encode_raw) else '#', range(encode_length)))
        input = np.array(list(
            map(lambda x: model[x] if x in model.wv.index2word else np.zeros(vector_size, dtype=float),
                encode_raw)))
        inputs.append(input.flatten())

    for decode_raw in data['decode']:
        label = np.zeros(label_size, dtype=float)
        np.put(label, decode_raw, 1)
        labels.append(label)
    return inputs, labels


def inference_embed(data):
    mecab = Okt()
    encode_raw = mecab.morphs(data)
    encode_raw = list(map(lambda x: encode_raw[x] if x < len(encode_raw) else '#', range(encode_length)))
    input = np.array(
        list(map(lambda x: model[x] if x in model.wv.index2word else np.zeros(vector_size, dtype=float), encode_raw)))
    return input


def get_test_data():
    train_data, train_label = embed(load_csv(train_data_list))
    test_data, test_label = embed(load_csv(train_data_list))
    return train_label, test_label, train_data, test_data


def create_graph(train=True):
    x = tf.placeholder("float", shape=[None, encode_length * vector_size], name='x')
    y_target = tf.placeholder("float", shape=[None, label_size], name='y_target')
    x_image = tf.reshape(x, [-1, encode_length, vector_size, 1], name="x_image")
    l2_loss = tf.constant(0.0)
    pooled_outputs = []
    for i, filter_size in enumerate(filter_sizes):
        with tf.name_scope("conv-maxpool-%s" % filter_size):
            filter_shape = [filter_size, vector_size, 1, num_filters]
            W_conv1 = tf.Variable(tf.truncated_normal(filter_shape, stddev=0.1), name="W")
            b_conv1 = tf.Variable(tf.constant(0.1, shape=[num_filters]), name="b")

            conv = tf.nn.conv2d(
                x_image,
                W_conv1,
                strides=[1, 1, 1, 1],
                padding="VALID",
                name="conv")

            h = tf.nn.relu(tf.nn.bias_add(conv, b_conv1), name="relu")
            pooled = tf.nn.max_pool(h,
                                    ksize=[1, encode_length - filter_size + 1, 1, 1],
                                    strides=[1, 1, 1, 1],
                                    padding='VALID',
                                    name="pool")
            pooled_outputs.append(pooled)

    num_filters_total = num_filters * len(filter_sizes)
    h_pool = tf.concat(pooled_outputs, 3)
    h_pool_flat = tf.reshape(h_pool, [-1, num_filters_total])
    keep_prob = 1.0
    if train:
        keep_prob = tf.placeholder("float", name="keep_prob")
        h_pool_flat = tf.nn.dropout(h_pool_flat, keep_prob)

    W_fc1 = tf.get_variable(
        "W_fc1",
        shape=[num_filters_total, label_size],
        initializer=tf.contrib.layers.xavier_initializer())
    b_fc1 = tf.Variable(tf.constant(0.1, shape=[label_size]), name="b")
    l2_loss += tf.nn.l2_loss(W_fc1)
    l2_loss += tf.nn.l2_loss(b_fc1)
    y = tf.nn.xw_plus_b(h_pool_flat, W_fc1, b_fc1, name="scores")
    predictions = tf.argmax(y, 1, name="predictions")
    losses = tf.nn.softmax_cross_entropy_with_logits(logits=y, labels=y_target)
    cross_entropy = tf.reduce_mean(losses)
    train_step = tf.train.AdamOptimizer(learning_rate).minimize(cross_entropy)
    correct_predictions = tf.equal(predictions, tf.argmax(y_target, 1))
    accuracy = tf.reduce_mean(tf.cast(correct_predictions, "float"), name="accuracy")

    return accuracy, x, y_target, keep_prob, train_step, y, cross_entropy, W_conv1


def train():
    try:
        labels_train, labels_test, data_filter_train, data_filter_test = get_test_data()
        tf.reset_default_graph()
        sess = tf.Session(config=tf.ConfigProto(gpu_options=tf.GPUOptions(allow_growth=True)))
        accuracy, x, y_target, keep_prob, train_step, y, cross_entropy, W_conv1 = create_graph(train=True)
        saver = tf.train.Saver(tf.all_variables())
        sess.run(tf.global_variables_initializer())

        for i in range(learning_step):
            sess.run(train_step, feed_dict={x: data_filter_train, y_target: labels_train, keep_prob: 0.5})
            if i % 100 == 0:
                train_accuracy = sess.run(accuracy,
                                          feed_dict={x: data_filter_train, y_target: labels_train, keep_prob: 1})
                print("step %d, training accuracy: %.3f" % (i, train_accuracy))

        path = './model/'
        if not os.path.exists(path):
            os.makedirs(path)
        saver.save(sess, path)
    except Exception as e:
        raise Exception("error on training: {0}".format(e))
    finally:
        sess.close()


def predict(test_data):
    try:
        tf.reset_default_graph()
        sess = tf.Session(config=tf.ConfigProto(gpu_options=tf.GPUOptions(allow_growth=True)))
        _, x, _, _, _, y, _, _ = create_graph(train=False)
        sess.run(tf.global_variables_initializer())
        saver = tf.train.Saver()
        path = './model/'
        if os.path.exists(path):
            saver.restore(sess, path)
        y = sess.run([y], feed_dict={x: np.array([test_data])})
        return format(np.argmax(y))
    except Exception as e:
        raise Exception("error on training: {0}".format(e))
    finally:
        sess.close()


def get_intent(text, is_train):
    if is_train:
        train()
    prediction = predict(np.array(inference_embed(tokenize(text))).flatten())
    return prediction
