import logging
import os

import tensorflow as tf
from flask import Flask, send_file, abort, request, jsonify # ensure request and jsonify are imported
from speech_to_text import recognize_speech
import speech_recognition as sr # For creating dummy audio data
from text_to_speech import synthesize_speech # Added import
from language_detection import detect_language_code # Added import

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
from chat.entity.news.entity_recognizer import get_news_entity
from chat.entity.restaurant.entity_recognizer import get_restaurant_entity
from chat.entity.song.entity_recognizer import get_song_entity
from chat.entity.translate.entity_recognizer import get_translate_entity
from chat.entity.weather.entity_recognizer import get_weather_entity
from chat.entity.wiki.entity_recognizer import get_wiki_entity
from chat.intent.classifier import get_intent
from chat.open_domain.open_domain import open_domain, get_emotion
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
######### DEEP LEARNING MODEL #########
##################################

@app.route('/intent/<language>/<text>', methods=['GET', 'POST'])
def server_intent(language, text):
    return get_intent(text, language=language)


@app.route('/open_domain/<language>/<text>', methods=['GET', 'POST'])
def server_open_domain(language, text):
    return open_domain(text, language=language)


@app.route('/emotion/<language>/<text>', methods=['GET', 'POST'])
def server_emotion(language, text):
    return get_emotion(text, language=language)


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
@app.route('/entity_dust/<language>/<text>', methods=['GET', 'POST'])
def server_dust_entity(language, text):
    # Assuming get_weather_entity is the correct call for dust entities
    return str(get_weather_entity(text, language=language, anago_on=False))


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
@app.route('/entity_translate/<language>/<text>', methods=['GET', 'POST'])
def server_translate_entity(language, text):
    return str(get_translate_entity(text, language=language, anago_on=False))


@app.route('/translate/<lang>/<text>', methods=['GET', 'POST'])
def server_translate(lang, text):
    return translate(lang=lang, text=text)


##################################
############# API : WIKI #############
##################################
@app.route('/entity_wiki/<language>/<text>', methods=['GET', 'POST'])
def server_wiki_entity(language, text):
    return str(get_wiki_entity(text, language=language, anago_on=False))


@app.route('/wiki/<language>/<text>', methods=['GET', 'POST'])
def server_wiki(language, text):
    return wiki(text, language=language)


##################################
########### API : WEATHER ############
##################################
@app.route('/entity_weather/<language>/<text>', methods=['GET', 'POST'])
def server_weather_entity(language, text):
    return str(get_weather_entity(text, language=language, anago_on=False))


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
@app.route('/entity_song/<language>/<text>', methods=['GET', 'POST'])
def server_song_entity(language, text):
    return str(get_song_entity(text, language=language, anago_on=False))


@app.route('/youtube/<language>/<text>', methods=['GET', 'POST'])
def server_youtube(language, text):
    return get_youtube(text, language=language)


##################################
############ API : NEWS ##############
##################################
@app.route('/entity_news/<language>/<text>', methods=['GET', 'POST'])
def server_news_entity(language, text):
    return str(get_news_entity(text, language=language, anago_on=False))


@app.route('/news/', methods=['GET', 'POST'])
def server_news():
    return get_news() # Assuming this is general news, not language specific text input


@app.route('/keyword_news/<language>/<keyword>', methods=['GET', 'POST'])
def server_keyword_news(language, keyword):
    return get_keyword_news(keyword, language=language)


##################################
########### API : RESTAURANT ###########
##################################
@app.route('/entity_restaurant/<language>/<text>', methods=['GET', 'POST'])
def server_restaurant_entity(language, text):
    return str(get_restaurant_entity(text, language=language, anago_on=False))


@app.route('/restaurant/<language>/<text>', methods=['GET', 'POST'])
def server_restaurant(language, text):
    return recommend_restaurant(text, language=language)


##################################
############ API : STT ###############
##################################
@app.route('/stt/<language>', methods=['POST']) # Should be POST to receive audio data
def server_stt(language):
    # In a real application, you'd get audio data from the POST request
    # e.g., if request.files['audio']:
    # audio_file = request.files['audio']
    # r = sr.Recognizer()
    # with sr.AudioFile(audio_file) as source:
    #     audio_data = r.record(source)
    # For now, let's simulate or return an error/placeholder

    # Placeholder for receiving audio data - actual implementation will depend on client-side
    # For testing the function call, we can't directly use a microphone here.
    # We'll assume audio_data is somehow received.
    # This is a simplified example; actual audio handling is more complex.

    # Simulate an error or a fixed response for now as we don't have live audio input
    if language not in ['en-US', 'es-ES', 'pt-BR']:
        return "Unsupported language", 400

    # This part is tricky without actual audio input.
    # Let's return a message indicating the service is ready but needs audio.
    # In a follow-up, we might test with a pre-recorded audio file if possible.
    return f"STT service for {language} is ready. Send audio data to this endpoint.", 200

    # Example of how it might be structured if we had audio_data:
    # text = recognize_speech(audio_data, language=language)
    # if text:
    #     return text
    # else:
    #     return "Could not recognize speech", 400

##################################
############ API : TTS ###############
##################################
@app.route('/tts/<language>/<text_to_speak>', methods=['GET'])
def server_tts(language, text_to_speak):
    if language not in ['en', 'es', 'pt']:
        return "Unsupported language for TTS", 400

    if not text_to_speak:
        return "No text provided for TTS", 400

    audio_filepath = synthesize_speech(text_to_speak, language=language)

    if audio_filepath:
        try:
            return send_file(audio_filepath, as_attachment=True, mimetype='audio/mpeg')
        except FileNotFoundError:
            abort(404, description="Generated audio file not found.")
        except Exception as e:
            print(f"Error sending TTS file: {e}")
            abort(500, description="Error sending TTS file.")
    else:
        return "Could not generate speech", 500

##################################
####### API : LANG DETECT ########
##################################
@app.route('/detect_language', methods=['POST'])
def server_detect_language():
    data = request.get_json()
    if not data or 'text' not in data:
        return jsonify({"error": "No text provided for language detection"}), 400

    text_to_detect = data['text']
    if not text_to_detect.strip():
        return jsonify({"error": "Text is empty"}), 400

    lang_code = detect_language_code(text_to_detect)

    if lang_code:
        # We are interested in 'en', 'es', 'pt'.
        # langdetect might return other codes (e.g., 'ca' for Catalan close to Spanish).
        # For now, we return what langdetect gives.
        # Client-side or further logic can map these if needed.
        return jsonify({"language_code": lang_code}), 200
    else:
        return jsonify({"error": "Could not detect language or text too short"}), 400

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=9890)
