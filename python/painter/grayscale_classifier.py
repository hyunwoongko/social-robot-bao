# Author : Hyunwoong
# When : 6/19/2019
# Homepage : github.com/gusdnd852
from painter.color_extractor import ColorExtractor


class GrayScaleClassifier:
    extractor = ColorExtractor()
    arr = []

    def is_gray(self, file):
        col = self.extractor.get_color(file, 3)
        arr = [i[1] for i in col]


if __name__ == '__main__':
    classifier = GrayScaleClassifier()
    classifier.is_gray('images/output/input.jpg')
