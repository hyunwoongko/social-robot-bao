from konlpy.tag import Okt


class IntentFinder:
    tokenizer = Okt()

    def __init__(self):
        self.sentence = ''

    def set_data(self, sentence):
        word_bag = []
        pos = self.tokenizer.pos(sentence, stem=True, norm=True)
        print(pos)

        for word, tag in pos:
            if tag == 'Josa' or tag == 'Adverb' or tag == 'Punctuation':
                continue
            else:
                word_bag.append(word)
        self.sentence = ' '.join(word_bag)
        print(self.sentence)


intent_finder = IntentFinder()
intent_finder.set_data("배고파")
