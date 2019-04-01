from konlpy.tag import Okt


def tokenize(sentence):
    tokenizer = Okt()
    word_bag = []
    pos = tokenizer.pos(sentence)
    for word, tag in pos:
        if (tag == 'Josa' and (word == '은' or word == '는' or word == '이' or word == '가' or
                               word == '로' or word == '으로' or word == '을' or word == '를')) or tag == 'Punctuation':
            continue
        else:
            word_bag.append(word)
    result = ' '.join(word_bag)
    return result
