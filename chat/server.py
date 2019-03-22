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
from entity_recognizer.entity_recognizer import get_entity
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
@app.route('/entity/<text>', methods=['GET', 'POST'])
def server_entity(text):
    return str(get_entity(text, False))


@app.route('/intent/<text>', methods=['GET', 'POST'])
def server_intent(text):
    return get_intent(text, False)


@app.route('/generate_answer/<text>', methods=['GET', 'POST'])
def server_generate_answer(text):
    return generate_answer(text)


@app.route('/train_entity', methods=['GET', 'POST'])
def train_entity():
    return get_entity("안녕하세요", True)


@app.route('/train_intent', methods=['GET', 'POST'])
def train_intent():
    return get_intent("안녕하세요", True)


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
@app.route('/translate/<lang>/<text>', methods=['GET', 'POST'])
def server_translate(lang, text):
    return translate(lang=lang, text=text)


##################################
############# API : WIKI #############
##################################
@app.route('/wiki/<text>', methods=['GET', 'POST'])
def server_wiki(text):
    return wiki(text)


##################################
########### API : WEATHER ############
##################################
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
@app.route('/youtube/<text>', methods=['GET', 'POST'])
def server_youtube(text):
    return get_youtube(text)


if __name__ == '__main__':
    app.run()
