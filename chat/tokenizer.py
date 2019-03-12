from konlpy.tag import Okt


def tokenize(sentence):
    tokenizer = Okt()
    word_bag = []
    pos = tokenizer.pos(sentence, stem=True, norm=True)

    for word, tag in pos:
        if tag == 'Josa' or tag == 'Adverb':
            continue
        else:
            word_bag.append(word)
    result = ' '.join(word_bag)
    return result
