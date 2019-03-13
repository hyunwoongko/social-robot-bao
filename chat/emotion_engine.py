import json

from konlpy.tag import Okt


def __get_emotion(wordname):
    with open('data/SentiWord_info.json', encoding='utf-8-sig', mode='r') as f:
        data = json.load(f)
    result = ['0', 0]
    for i in range(0, len(data)):
        if data[i]['word'] == wordname:
            result.pop()
            result.pop()
            result.append(data[i]['word_root'])
            result.append(data[i]['polarity'])

    s_word = result[1]
    return int(s_word)


def get_emotion(sentence):
    tokenizer = Okt()
    pos = tokenizer.pos(sentence)
    word_bag = [word for word, _ in pos]
    emotion_bag = [__get_emotion(word) for word in word_bag]
    is_emotional = False
    for emotion in emotion_bag:
        if emotion != 0:
            is_emotional = True

    if is_emotional:
        while 0 in emotion_bag:
            emotion_bag.remove(0)
        return sum(emotion_bag) / len(emotion_bag)
    else:
        return 0
