import logging
import os
import random
import shutil

import tensorflow as tf
from flask import Flask, render_template, Response

from api.api_dust import today_dust, tomorrow_dust, after_tomorrow_dust
from api.api_exchange import get_exchange
from api.api_issue import get_issue
from api.api_news import get_news, get_keyword_news
from api.api_restaurant import recommend_restaurant
from api.api_translation import translate
from api.api_weather import today_weather, tomorrow_weather, after_tomorrow_weather, this_week_weather, specific_weather
from api.api_wiki import wiki
from api.api_wise import get_wise
from api.api_youtube import get_youtube
from camera_pi import Camera
from entity_recognizer.dust.entity_recognizer import get_dust_entity
from entity_recognizer.exchange.entity_recognizer import get_exchange_entity
from entity_recognizer.news.entity_recognizer import get_news_entity
from entity_recognizer.restaurant.entity_recognizer import get_restaurant_entity
from entity_recognizer.song.entity_recognizer import get_song_entity
from entity_recognizer.translate.entity_recognizer import get_translate_entity
from entity_recognizer.weather.entity_recognizer import get_weather_entity
from entity_recognizer.wiki.entity_recognizer import get_wiki_entity
from generative_model.answer_generator import generate_answer
from hanspell.spell_checker import fix
from intent_classifier.intent_classifier import get_intent
from markov_engine import MarkovEngine
from util.list_util import mode
from util.tokenizer import tokenize

if os.path.isdir('data_out'):
    shutil.rmtree('data_out')
shutil.copytree('generative_model/data_out', 'data_out')

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


@app.route('/generate_answer/<userid>/<text>', methods=['GET', 'POST'])
def server_generate_answer(userid, text):
    engine = MarkovEngine(userid)
    normal = fix(generate_answer(text)).rstrip()
    counter = 0
    markov_list = []
    while True:
        counter += 1
        markov = engine.apply_markov(text)
        if markov != text:
            markov_list.append(markov)
        if len(markov_list) == 3:
            break
        if counter > 5:
            while True:
                markov_list.append(markov)
                if len(markov_list) >= 3:
                    break

    answer = [normal, normal, fix(engine.apply_markov(normal)), fix(markov_list[0]), fix(markov_list[1]),
              fix(markov_list[2])]
    print(answer)  # 전체 답변 후보 출력
    result = mode(answer)  # 최빈 값 출력
    if len(answer) == 1:
        return result[0]  # 최빈 값 출력
    else:  # 데이터와 마르코프 최빈값의 결과 수 가 같을때
        return random.choice(result)  # 랜덤 출력


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


##################################
############ API : NEWS ############
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
############ API : RESTAURANT ############
##################################
@app.route('/entity_restaurant/<text>', methods=['GET', 'POST'])
def server_restaurant_entity(text):
    return str(get_restaurant_entity(text, False))


@app.route('/restaurant/<text>', methods=['GET', 'POST'])
def server_restaurant(text):
    return recommend_restaurant(text)


##################################
############ API : EXCHANGE ############
##################################
@app.route('/entity_exchange/<text>', methods=['GET', 'POST'])
def server_exchange_entity(text):
    return str(get_exchange_entity(text, False))


@app.route('/exchange/<text>', methods=['GET', 'POST'])
def server_exchange(text):
    return get_exchange(text)


@app.route('/eye')
def eye():
    """Video streaming home page."""
    return render_template('index.html')


def gen(camera):
    """Video streaming generator function."""
    while True:
        frame = camera.get_frame()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')


@app.route('/video_feed')
def video_feed():
    """Video streaming route. Put this in the src attribute of an img tag."""
    return Response(gen(Camera()),
                    mimetype='multipart/x-mixed-replace; boundary=frame')


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=9892)
