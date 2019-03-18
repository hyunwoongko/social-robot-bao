import api.api_weather as weather
from hanspell.spell_checker import fix
from util import tokenizer


def response(named_entity):
    keyword_group = named_entity[0]
    entity_group = named_entity[1]
    WORD = []
    LANG = ''

    for k in zip(keyword_group, entity_group):
        if 'WORD' in k[1]:
            WORD.append(k[0])
        elif 'LANG' in k[1]:
            LANG = k[0]

    if len(LANG) == 0:
        LANG = 'en'

    if len(LANG) == 0:
        while len(LANG) == 0:
            print('> ' + fix('어떤 언어로 알려드릴까요 : '), end='')
            lang = input()
            if lang is not None and lang.replace(' ', '') != '':
                LANG = lang

    return translate()
