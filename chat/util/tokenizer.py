from konlpy.tag import Okt


def tokenize(sentence):
    tokenizer = Okt()
    word_bag = []
    pos = tokenizer.pos(sentence,stem=True)

    for word, tag in pos:
        if tag == 'Josa' or tag == 'Punctuation' or tag == 'Eomi' or tag == "Foreign" or tag == "바오":
            continue
        else:
            word_bag.append(word)
    result = ' '.join(word_bag)
    return result
