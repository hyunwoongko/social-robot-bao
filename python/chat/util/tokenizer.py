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
        elif (tag == 'Josa' and (word == '이네' or word == '이구나' or word == '이야' or
                                 word == '은' or word == '는' or word == '이' or word == '가' or
                                 word == '로' or word == '으로' or word == '을' or word == '를')) or tag == 'Punctuation':
            continue
        else:
            word_bag.append(word)
    result = ''.join(word_bag)
    return result
