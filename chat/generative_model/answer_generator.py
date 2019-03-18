import tensorflow as tf

import generative_model.data as data
import generative_model.model as ml
from generative_model.configs import DEFINES
from hanspell.spell_checker import fix

tf.logging.set_verbosity(tf.logging.ERROR)
char2idx, idx2char, vocabulary_length = data.load_vocabulary('predict')


def generate_answer(text):
    saved_answer = ''
    predic_input_enc, predic_input_enc_length = data.enc_processing([text], char2idx, 'predict')
    predic_output_dec, predic_output_dec_length = data.dec_output_processing([""], char2idx)
    predic_target_dec = data.dec_target_processing([""], char2idx)
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

    answer = ''
    for i in range(10):
        if i > 0:
            predic_output_dec, predic_output_decLength = data.dec_output_processing([answer], char2idx)
            predic_target_dec = data.dec_target_processing([answer], char2idx)

        predictions = classifier.predict(
            input_fn=lambda: data.eval_input_fn(predic_input_enc, predic_output_dec, predic_target_dec, 1))

        answer = data.pred_next_string(predictions, idx2char)
        if len(answer) == len(saved_answer):
            break
        else:
            saved_answer = answer

    return fix(answer)


if __name__ == '__main__':
    raise Exception('이 파일 실행하지 마시고 위의 함수 실행하세요')
