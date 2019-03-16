import scenario.scenario_weather as weather


def run_app():
    while True:
        print("\n> 입력 하세요 : ", end='')
        question = input()
        question = tokenize(question)
        question = fix(question)

        intent = get_intent(question, is_train=False)
        entity = get_entity(question, is_train=False)
        emotion = get_emotion(question)

        print("\n전처리 문장 : ", question)
        print('발화 의도 : ', intent)
        print('개체 분류 : ', entity)
        print('감정 지수 (-2 ~ 2) : ', emotion)

        if intent == '날씨':
            print('> ' + fix(weather.response(entity)))


if __name__ == '__main__':
    print("깨어나는 중입니다...")
    from os import environ;environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
    import tensorflow as tf;tf.logging.set_verbosity(tf.logging.ERROR)
    from emotion_engine.emotion_engine import get_emotion
    from entity_recognizer.entity_recognizer import get_entity
    from hanspell.spell_checker import fix
    from intent_classifier.intent_classifier import get_intent
    from util.tokenizer import tokenize

    run_app()


