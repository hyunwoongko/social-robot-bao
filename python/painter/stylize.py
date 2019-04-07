from __future__ import division, print_function

import sys

sys.path.append('./wct/')
from painter.wct.utils import get_img, save_img, resize_to
from painter.wct.wct import WCT

checkpoints = ['models/wct/relu5_1', 'models/wct/relu4_1',
               'models/wct/relu3_1', 'models/wct/relu2_1', 'models/wct/relu1_1']
relu_targets = ['relu5_1', 'relu4_1', 'relu3_1', 'relu2_1', 'relu1_1']

# Load the WCT model
wct_model = WCT(checkpoints=checkpoints,
                relu_targets=relu_targets,
                vgg_path='models/wct/vgg_normalised.t7',
                device='/cpu:0',
                ss_patch_size=3,
                ss_stride=1)


def get_stylize_image(content_fullpath, style_fullpath, output_path,
                      content_size=256, style_size=256, alpha=0.6,
                      swap5=False, ss_alpha=0.6, adain=False):
    content_img = get_img(content_fullpath)
    content_img = resize_to(content_img, content_size)

    style_img = get_img(style_fullpath)
    style_img = resize_to(style_img, style_size)

    stylized_rgb = wct_model.predict(
        content_img, style_img, alpha, swap5, ss_alpha, adain)

    save_img(output_path, stylized_rgb)


styles = ['anime', 'monet', 'vangogh', 'eastern', 'snow', 'mondrian', 'monk', 'fall', 'light', 'sakura']


def main(n):
    num = str(n)
    get_stylize_image('1.png', './styles/' + num + '.jpg', '2-' + num + '.png')


if __name__ == '__main__':
    for i in styles:
        main(i)
