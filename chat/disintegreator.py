from konlpy.tag import Okt


def tokenize(sentence):
    tokenizer = Okt()
    word_bag = []
    pos = tokenizer.pos(sentence)

    for word, tag in pos:
        if tag == 'Josa' or tag == 'Punctuation' or tag == 'Eomi' or tag == "Foreign":
            continue
        else:
            word_bag.append(word)
    result = ' '.join(word_bag)
    return result

def disintegrate(sentence):
    tokenizer = Okt()
    word_bag = []
    pos = tokenizer.pos(sentence)
    for word, tag in pos:
        word_bag.append(word)
    result = ' '.join(word_bag)
    return result
