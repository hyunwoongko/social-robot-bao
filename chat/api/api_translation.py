from googletrans import Translator

translator = Translator()


def translate(text, lang, src='ko'):
    return translator.translate(text, src=src, dest=lang).text
