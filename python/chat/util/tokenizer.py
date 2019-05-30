from konlpy.tag import Okt

stop_word = [
    '바오'
]


def tokenize(sentence):
    tokenizer = Okt()
    word_bag = []
    pos = tokenizer.pos(sentence)
    for word, tag in pos:
        if word in stop_word:
            continue
        elif tag == 'Josa' or tag == 'Punctuation':
            continue
        else:
            word_bag.append(word)
    result = ''.join(word_bag)
    return result
