from api.api_translation import translate
from requester import request
from hanspell.spell_checker import fix


def response(named_entity):
    keyword_group = named_entity[0]
    entity_group = named_entity[1]
    lang = []
    target = []

    for k in zip(keyword_group, entity_group):
        if 'WORD' in k[1]:
            target.append(k[0])
        elif 'LANG' in k[1]:
            lang = k[0]

    if len(target) == 0:
        while len(target) == 0:
            print('> ' + fix('어떤 말을 알려드릴까요 : '), end='')
            target_input = request()
            if target_input is not None and target_input.replace(' ', '') != '':
                target.append(target_input)

    if lang is None or len(lang) == 0:
        while lang is None or len(lang) == 0:
            print('> ' + fix('어떤 언어로 말해드릴까요 : '), end='')
            lang_input = request()
            if lang_input is not None and lang_input.replace(' ', '') != '':
                lang.append(lang_input)

    if '영어' in lang:
        lang = 'en'
    elif '일본어' in lang or '일본' in lang:
        lang = 'ja'
    elif '중국어' in lang or '중국' in lang:
        lang = 'zh-CN'
    elif '스페인어' in lang or '스페인' in lang:
        lang = 'es'
    else:
        return lang[0] + '은 아직 배우고있답니다.'

    return translate(' '.join(target), lang)
