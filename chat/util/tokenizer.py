from konlpy.tag import Okt

from hanspell.spell_checker import fix


def tokenize(sentence):
    tokenizer = Okt()
    word_bag = []
    pos = tokenizer.pos(sentence, norm=True)

    for word, tag in pos:
        if tag == 'Josa' or tag == 'Punctuation' or tag == "Foreign" or tag == "바오":
            continue
        else:
            word_bag.append(word)
    result = ' '.join(word_bag)
    return result
