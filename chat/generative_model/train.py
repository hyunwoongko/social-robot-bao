import os
import shutil

import tensorflow as tf

import generative_model.data as data
import generative_model.model as ml
from generative_model.configs import DEFINES

DATA_OUT_PATH = './data_out/'


def main(self):
    data_out_path = os.path.join(os.getcwd(), DATA_OUT_PATH)
    os.makedirs(data_out_path, exist_ok=True)
    # 데이터를 통한 사전 구성 한다.
    char2idx, idx2char, vocabulary_length = data.load_vocabulary('train')
    # 훈련 데이터와 테스트 데이터를 가져온다.
    train_input, train_label, eval_input, eval_label = data.load_data()

    # 훈련셋 인코딩 만드는 부분이다.
    train_input_enc, train_input_enc_length = data.enc_processing(train_input, char2idx, 'train')
    # 훈련셋 디코딩 입력 부분 만드는 부분이다.
    train_output_dec, train_output_dec_length = data.dec_output_processing(train_label, char2idx)
    # 훈련셋 디코딩 출력 부분 만드는 부분이다.
    train_target_dec = data.dec_target_processing(train_label, char2idx)

    # 평가셋 인코딩 만드는 부분이다.
    eval_input_enc, eval_input_enc_length = data.enc_processing(eval_input, char2idx, 'train')
    # 평가셋 인코딩 만드는 부분이다.
    eval_output_dec, eval_output_dec_length = data.dec_output_processing(eval_label, char2idx)
    # 평가셋 인코딩 만드는 부분이다.
    eval_target_dec = data.dec_target_processing(eval_label, char2idx)

    # 현재 경로'./'에 현재 경로 하부에 
    # 체크 포인트를 저장한 디렉토리를 설정한다.
    check_point_path = os.path.join(os.getcwd(), DEFINES.check_point_path)
    # 디렉토리를 만드는 함수이며 두번째 인자 exist_ok가 
    # True이면 디렉토리가 이미 존재해도 OSError가 
    # 발생하지 않는다.
    # exist_ok가 False이면 이미 존재하면 
    # OSError가 발생한다.
    os.makedirs(check_point_path, exist_ok=True)

    # 에스티메이터 구성한다.
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

    # 학습 실행
    classifier.train(input_fn=lambda: data.train_input_fn(
        train_input_enc, train_output_dec, train_target_dec, DEFINES.batch_size), steps=DEFINES.train_steps)

    eval_result = classifier.evaluate(input_fn=lambda: data.eval_input_fn(
        eval_input_enc, eval_output_dec, eval_target_dec, DEFINES.batch_size))
    print('\nEVAL set accuracy: {accuracy:0.3f}\n'.format(**eval_result))

if __name__ == '__main__':
    if os.path.exists('data_out/vocabularyData.voc'):
        shutil.rmtree('data_out/vocabularyData.voc')
    char2idx, idx2char, vocabulary_length = data.load_vocabulary('data')
    tf.logging.set_verbosity(tf.logging.INFO)
    tf.app.run(main)


tf.logging.set_verbosity
