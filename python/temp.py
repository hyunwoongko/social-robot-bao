# Author : Hyunwoong
# When : 5/30/2019
# Homepage : github.com/gusdnd852
from chat.crawler.dust import metropolitan, tomorrow_dust
from chat.util.hanspell.spell_checker import fix
from chat.util.tokenizer import tokenize

if __name__ == '__main__':
    while True:
        q = fix(tokenize(fix(input())))
        print(q)
        a = tomorrow_dust(q)
        print(a)
