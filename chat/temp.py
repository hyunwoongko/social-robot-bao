from hanspell.spell_checker import fix
from util.tokenizer import tokenize

if __name__ == '__main__':
    while True:
        question = input()
        question = tokenize(question)
        question = fix(question)
        print(question)
