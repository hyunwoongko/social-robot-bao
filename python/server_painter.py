import urllib.request

from flask import Flask
from flask import send_file

from painter.pix2pix import pix_translate
from painter.stylize import Stylizer

app = Flask(__name__)


@app.route('/')
def init():
    return 'BAO SERVER ON - PAINTER'


@app.route('/normal/<string:uid>/<path:url>')
def normal(uid, url):
    file = uid + '.jpg'
    input_path = 'painter/images/input/'+file
    output_path = 'painter/images/output/'+file
    urllib.request.urlretrieve(url + '?alt=media', input_path)
    pix_translate(input_path, output_path)
    stylizer = Stylizer(file)
    stylizer.normal_stylize()
    return send_file(output_path, mimetype='image/png')


@app.route('/random/<string:uid>/<path:url>')
def random(uid, url):
    file = uid + '.jpg'
    input_path = 'painter/images/input/'+file
    output_path = 'painter/images/output/'+file
    urllib.request.urlretrieve(url + '?alt=media', input_path)
    pix_translate(input_path, output_path)
    stylizer = Stylizer(file)
    stylizer.random_stylize()
    return send_file(output_path, mimetype='image/png')


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=9891)
