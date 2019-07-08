import random
import urllib.request
from datetime import datetime
from functools import wraps, update_wrapper

from flask import Flask, make_response
from flask import send_file

from painter.gray_classifier import GrayscaleClassifier
from painter.pix2pix import pix_translate
from painter.stylize import Stylizer

app = Flask(__name__)
app.config['SEND_FILE_MAX_AGE_DEFAULT'] = 0


def nocache(view):
    @wraps(view)
    def no_cache(*args, **kwargs):
        response = make_response(view(*args, **kwargs))
        response.headers['Last-Modified'] = datetime.now()
        response.headers['Cache-Control'] = 'no-store, no-cache, must-revalidate, post-check=0, pre-check=0, max-age=0'
        response.headers['Pragma'] = 'no-cache'
        response.headers['Expires'] = '-1'
        return response

    return update_wrapper(no_cache, view)


@app.route('/')
def init():
    return 'BAO SERVER ON - PAINTER'


@app.route('/draw/<string:uid>/<path:url>')
@nocache
def draw(uid, url):
    file = uid + '.jpg'
    input_path = 'painter/images/input/' + file
    output_path = 'painter/images/output/' + file
    urllib.request.urlretrieve(url + '?alt=media', input_path)
    pix_translate(input_path, output_path)
    classifier = GrayscaleClassifier()
    is_gray = classifier.is_gray(output_path)
    stylizer = Stylizer(file)
    rd = random.randint(0, 1)
    print("RANDOM VALUE IS {0}".format(rd))
    if is_gray:
        stylizer.stylize(True)
        print("GRAY SCALE IMAGE !!")
    elif rd == 0:
        stylizer.stylize(True)
        print("RANDOM STYLIZE !!".format(rd))
    return send_file(output_path, mimetype='image/png')


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=9891)
