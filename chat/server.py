import logging
import os

import tensorflow as tf
from flask import Flask

from api.api_dust import today_dust, tomorrow_dust, after_tomorrow_dust
from api.api_issue import get_issue
from api.api_translation import translate
from api.api_weather import today_weather, tomorrow_weather, after_tomorrow_weather, this_week_weather, specific_weather
from api.api_wiki import wiki
from api.api_wise import get_wise
from api.api_youtube import get_youtube
from entity_recognizer.dust.entity_recognizer import get_dust_entity
from entity_recognizer.song.entity_recognizer import get_song_entity
from entity_recognizer.translate.entity_recognizer import get_translate_entity
from entity_recognizer.weather.entity_recognizer import get_weather_entity
from entity_recognizer.wiki.entity_recognizer import get_wiki_entity
from generative_model.answer_generator import generate_answer
from hanspell.spell_checker import fix
from intent_classifier.intent_classifier import get_intent
from util.tokenizer import tokenize

logger = logging.getLogger('chardet')
logger.setLevel(logging.CRITICAL)
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
tf.logging.set_verbosity(tf.logging.ERROR)
app = Flask(__name__)


@app.route('/')
def init():
    return 'BAO SERVER ON - WELFARE MANAGEMENT'


##################################
######### DEEP LEARNING MODEL #########
##################################

@app.route('/intent/<text>', methods=['GET', 'POST'])
def server_intent(text):
    return get_intent(text, False)


@app.route('/generate_answer/<text>', methods=['GET', 'POST'])
def server_generate_answer(text):
    return generate_answer(text)


##################################
############ PREPROCESS #############
##################################
@app.route('/tokenize/<text>', methods=['GET', 'POST'])
def server_tokenize(text):
    return tokenize(text)


@app.route('/fix/<text>', methods=['GET', 'POST'])
def server_fix(text):
    return fix(text)


##################################
############# API : DUST #############
##################################
@app.route('/entity_dust/<text>', methods=['GET', 'POST'])
def server_dust_entity(text):
    return str(get_dust_entity(text, False))


@app.route('/today_dust/<location>', methods=['GET', 'POST'])
def server_today_dust(location):
    return today_dust(location)


@app.route('/tomorrow_dust/<location>', methods=['GET', 'POST'])
def server_tomorrow_dust(location):
    return tomorrow_dust(location)


@app.route('/after_tomorrow_dust/<location>', methods=['GET', 'POST'])
def server_after_tomorrow_dust(location):
    return after_tomorrow_dust(location)


##################################
############# API : ISSUE #############
##################################
@app.route('/issue', methods=['GET', 'POST'])
def server_issue():
    return get_issue()


##################################
########### API : TRANSLATE ###########
##################################
@app.route('/entity_translate/<text>', methods=['GET', 'POST'])
def server_translate_entity(text):
    return str(get_translate_entity(text, False))


@app.route('/translate/<lang>/<text>', methods=['GET', 'POST'])
def server_translate(lang, text):
    return translate(lang=lang, text=text)


##################################
############# API : WIKI #############
##################################
@app.route('/entity_wiki/<text>', methods=['GET', 'POST'])
def server_wiki_entity(text):
    return str(get_wiki_entity(text, False))


@app.route('/wiki/<text>', methods=['GET', 'POST'])
def server_wiki(text):
    return wiki(text)


##################################
########### API : WEATHER ############
##################################
@app.route('/entity_weather/<text>', methods=['GET', 'POST'])
def server_weather_entity(text):
    return str(get_weather_entity(text, False))


@app.route('/today_weather/<location>', methods=['GET', 'POST'])
def server_today_weather(location):
    return today_weather(location)


@app.route('/tomorrow_weather/<location>', methods=['GET', 'POST'])
def server_tomorrow_weather(location):
    return tomorrow_weather(location)


@app.route('/after_tomorrow_weather/<location>', methods=['GET', 'POST'])
def server_after_tomorrow_weather(location):
    return after_tomorrow_weather(location)


@app.route('/this_week_weather/<location>', methods=['GET', 'POST'])
def server_this_week_weather(location):
    return this_week_weather(location)


@app.route('/specific_weather/<location>/<date>', methods=['GET', 'POST'])
def server_specific_weather(location, date):
    return specific_weather(location, date)


##################################
############# API : WISE #############
##################################
@app.route('/wise', methods=['GET', 'POST'])
def server_wise():
    return get_wise()


##################################
############ API : YOUTUBE ############
##################################
@app.route('/entity_song/<text>', methods=['GET', 'POST'])
def server_song_entity(text):
    return str(get_song_entity(text, False))


@app.route('/youtube/<text>', methods=['GET', 'POST'])
def server_youtube(text):
    return get_youtube(text)


if __name__ == '__main__':
    app.run()
