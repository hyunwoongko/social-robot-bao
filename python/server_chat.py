import logging
import os

import tensorflow as tf
from flask import Flask

from chat.crawler.dust import today_dust, tomorrow_dust, after_tomorrow_dust
from chat.crawler.issue import get_issue
from chat.crawler.news import get_news, get_keyword_news
from chat.crawler.restaurant import recommend_restaurant
from chat.crawler.translation import translate
from chat.crawler.weather import today_weather, tomorrow_weather, after_tomorrow_weather, this_week_weather, \
    specific_weather
from chat.crawler.wiki import wiki
from chat.crawler.wise import get_wise
from chat.crawler.youtube import get_youtube
from chat.doc2vec.similarity import get_similarity
from chat.entity.news.entity_recognizer import get_news_entity
from chat.entity.restaurant.entity_recognizer import get_restaurant_entity
from chat.entity.song.entity_recognizer import get_song_entity
from chat.entity.translate.entity_recognizer import get_translate_entity
from chat.entity.weather.entity_recognizer import get_weather_entity
from chat.entity.wiki.entity_recognizer import get_wiki_entity
from chat.intent.classifier import get_intent
from chat.util.hanspell.spell_checker import fix
from chat.util.tokenizer import tokenize

logger = logging.getLogger('chardet')
logger.setLevel(logging.CRITICAL)
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
tf.logging.set_verbosity(tf.logging.ERROR)
app = Flask(__name__)


@app.route('/')
def init():
    return 'BAO SERVER ON - CHAT'


##################################
####### DEEP LEARNING MODEL #######
##################################

@app.route('/intent/<text>', methods=['GET', 'POST'])
def server_intent(text):
    return get_intent(text)


@app.route('/similarity/<text>', methods=['GET', 'POST'])
def server_similarity(text):
    return get_similarity(text)


##################################
######## PREPROCESS ###########
##################################
@app.route('/tokenize/<text>', methods=['GET', 'POST'])
def server_tokenize(text):
    return tokenize(text)


@app.route('/fix/<text>', methods=['GET', 'POST'])
def server_fix(text):
    return fix(text)


##################################
########### API : DUST ###########
##################################
@app.route('/entity_dust/<text>', methods=['GET', 'POST'])
def server_dust_entity(text):
    return str(get_weather_entity(text, False))


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
########### API : ISSUE ###########
##################################
@app.route('/issue', methods=['GET', 'POST'])
def server_issue():
    return get_issue()


##################################
######### API : TRANSLATE #########
##################################
@app.route('/entity_translate/<text>', methods=['GET', 'POST'])
def server_translate_entity(text):
    return str(get_translate_entity(text, False))


@app.route('/translate/<lang>/<text>', methods=['GET', 'POST'])
def server_translate(lang, text):
    return translate(lang=lang, text=text)


##################################
########### API : WIKI ###########
##################################
@app.route('/entity_wiki/<text>', methods=['GET', 'POST'])
def server_wiki_entity(text):
    return str(get_wiki_entity(text, False))


@app.route('/wiki/<text>', methods=['GET', 'POST'])
def server_wiki(text):
    return wiki(text)


##################################
######### API : WEATHER ##########
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
########### API : WISE ###########
##################################
@app.route('/wise', methods=['GET', 'POST'])
def server_wise():
    return get_wise()


##################################
########## API : YOUTUBE ##########
##################################
@app.route('/entity_song/<text>', methods=['GET', 'POST'])
def server_song_entity(text):
    return str(get_song_entity(text, False))


@app.route('/youtube/<text>', methods=['GET', 'POST'])
def server_youtube(text):
    return get_youtube(text)


##################################
########## API : NEWS ##########
##################################
@app.route('/entity_news/<text>', methods=['GET', 'POST'])
def server_news_entity(text):
    return str(get_news_entity(text, False))


@app.route('/news/', methods=['GET', 'POST'])
def server_news():
    return get_news()


@app.route('/keyword_news/<keyword>', methods=['GET', 'POST'])
def server_keyword_news(keyword):
    return get_keyword_news(keyword)


##################################
######### API : RESTAURANT #########
##################################
@app.route('/entity_restaurant/<text>', methods=['GET', 'POST'])
def server_restaurant_entity(text):
    return str(get_restaurant_entity(text, False))


@app.route('/restaurant/<text>', methods=['GET', 'POST'])
def server_restaurant(text):
    return recommend_restaurant(text)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=9890)
