from api.api_translation import translate
from hanspell.spell_checker import fix


def response(named_entity):
    keyword_group = named_entity[0]
    entity_group = named_entity[1]
    lang = None
    target = []

    for k in zip(keyword_group, entity_group):
        if 'WORD' in k[1]:
            target.append(k[0])
        elif 'LANG' in k[1]:
            lang = k[0]

    if len(target) == 0:
        while len(target) == 0:
            print('> ' + fix('어떤 말을 알려드릴까요 : '), end='')
            target_input = input()
            if target_input is not None and target_input.replace(' ', '') != '':
                target.append(target_input)

    if lang is None or len(lang) == 0:
        while lang is None or len(lang) == 0:
            print('> ' + fix('어떤 언어로 말해드릴까요 : '), end='')
            lang_input = input()
            if lang is not None and lang.replace(' ', '') != '':
                lang = lang_input

    if '일본' in lang:
        lang = 'ja'
    elif '영어' in lang:
        lang = 'en'
    elif '불어' in lang or '프랑스' in lang:
        lang = 'fr'
    elif '독일' in lang or '독어' in lang:
        lang = 'de'

    return translate(' '.join(target), lang)
