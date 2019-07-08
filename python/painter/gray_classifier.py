# Author : Hyunwoong
# When : 6/19/2019
# Homepage : github.com/gusdnd852

from painter.grayscale.color_extractor import ColorExtractor


class GrayscaleClassifier:
    extractor = ColorExtractor()
    arr = []

    def is_gray(self, file):
        col = self.extractor.get_color(file, 3)
        arr = [i[1] for i in col]

        gfactor = 0
        for i in arr:
            gfactor += (abs(i[0] - i[1]))
            gfactor += (abs(i[1] - i[2]))
            gfactor += (abs(i[0] - i[2]))
        print('grayfactor is ' + str(gfactor))
        if gfactor < 50:
            return True
        else:
            return False
