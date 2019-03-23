from entity_recognizer.dust.entity_recognizer import get_dust_entity
from entity_recognizer.song.entity_recognizer import get_song_entity
from entity_recognizer.translate.entity_recognizer import get_translate_entity
from entity_recognizer.weather.entity_recognizer import get_weather_entity
from entity_recognizer.wiki.entity_recognizer import get_wiki_entity
from hanspell.spell_checker import fix
from intent_classifier.intent_classifier import get_intent
from util.tokenizer import tokenize


def train_intent():
    return get_intent(tokenize(fix('오늘 날씨 어때')), True)


def train_dust():
    return get_dust_entity(tokenize(fix("오늘 전주 미세먼지 농도 알려줄래")), True)


def train_weather():
    return get_weather_entity(tokenize(fix("오늘 전주 날씨 알려주라")), True)


def train_song():
    return get_song_entity(tokenize(fix("슬픈음악 들려줘")), True)


def train_wiki():
    return get_wiki_entity(tokenize(fix("전현무가 누구야")), True)


def train_translate():
    return get_translate_entity(tokenize(fix("아름다운 하늘 이 문장 영어로 번역해줘")), True)


if __name__ == '__main__':
    # d = train_dust()
    # w = train_weather()
    # s = train_song()
    # w = train_wiki()
    # t = train_translate()
    i = train_intent()
