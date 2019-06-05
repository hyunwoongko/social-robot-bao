import urllib.request

from flask import Flask
from flask import send_file

from painter.pix2pix import pix_translate
from painter.stylize import Stylizer

app = Flask(__name__)


@app.route('/')
def init():
    return 'BAO SERVER ON - PAINTER'


@app.route('/download/<string:uid>/<path:url>')
def download_image(uid, url):
    file = 'painter/images/input/' + uid + '.jpg'
    urllib.request.urlretrieve(url, file)
    return url


@app.route('/normal/<file>')
def normal(file):
    pix_translate(file, file)
    stylizer = Stylizer(file)
    stylizer.normal_stylize()
    output = 'painter/images/output/' + file
    return send_file(output, mimetype='image/png')


@app.route('/random/<file>')
def random(file):
    pix_translate(file, file)
    stylizer = Stylizer(file)
    stylizer.random_stylize()
    output = 'painter/images/output/' + file
    return send_file(output, mimetype='image/png')


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=9891)
