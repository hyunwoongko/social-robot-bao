import os

import tensorflow as tf

import data
import generative_model.model as ml
from generative_model.configs import DEFINES

DATA_OUT_PATH = 'data_out/'


def main(self):
    data_out_path = os.path.join(os.getcwd(), DATA_OUT_PATH)
    os.makedirs(data_out_path, exist_ok=True)
    char2idx, idx2char, vocabulary_length = data.load_vocabulary()
    train_input, train_label, eval_input, eval_label = data.load_data()

    train_input_enc, train_input_enc_length = data.enc_processing(train_input, char2idx)
    train_output_dec, train_output_dec_length = data.dec_output_processing(train_label, char2idx)
    train_target_dec = data.dec_target_processing(train_label, char2idx)

    eval_input_enc, eval_input_enc_length = data.enc_processing(eval_input, char2idx)
    eval_output_dec, eval_output_dec_length = data.dec_output_processing(eval_label, char2idx)
    eval_target_dec = data.dec_target_processing(eval_label, char2idx)
    check_point_path = os.path.join(os.getcwd(), DEFINES.check_point_path)
    os.makedirs(check_point_path, exist_ok=True)

    classifier = tf.estimator.Estimator(
        model_fn=ml.Model,  # 모델 등록한다.
        model_dir=DEFINES.check_point_path,  # 체크포인트 위치 등록한다.
        params={  # 모델 쪽으로 파라메터 전달한다.
            'hidden_size': DEFINES.hidden_size,  # 가중치 크기 설정한다.
            'learning_rate': DEFINES.learning_rate,  # 학습율 설정한다.
            'vocabulary_length': vocabulary_length,  # 딕셔너리 크기를 설정한다.
            'embedding_size': DEFINES.embedding_size,  # 임베딩 크기를 설정한다.
            'max_sequence_length': DEFINES.max_sequence_length,
        })


    classifier.train(input_fn=lambda:data.train_input_fn(
        train_input_enc, train_output_dec, train_target_dec,  DEFINES.batch_size), steps=DEFINES.train_steps)

    eval_result = classifier.evaluate(input_fn=lambda: data.eval_input_fn(
        eval_input_enc, eval_output_dec, eval_target_dec, DEFINES.batch_size))
    print('\nEVAL set accuracy: {accuracy:0.3f}\n'.format(**eval_result))
    predic_input_enc, predic_input_enc_length = data.enc_processing(["여자친구 만나고싶다"], char2idx)
    predic_output_dec, predic_output_decLength = data.dec_output_processing([""], char2idx)
    predic_target_dec = data.dec_target_processing([""], char2idx)

    for i in range(DEFINES.max_sequence_length):
        if i > 0:
            predic_output_dec, predic_output_decLength = data.dec_output_processing([answer], char2idx)
            predic_target_dec = data.dec_target_processing([answer], char2idx)

        predictions = classifier.predict(
            input_fn=lambda: data.eval_input_fn(predic_input_enc, predic_output_dec, predic_target_dec, 1))

        answer = data.pred_next_string(predictions, idx2char)
    print("answer: ", answer)


if __name__ == '__main__':
    tf.logging.set_verbosity(tf.logging.INFO)
    tf.app.run(main)