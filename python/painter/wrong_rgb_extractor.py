# Author : Hyunwoong
# When : 6/19/2019
# Homepage : github.com/gusdnd852
import os

import pandas as pd

from painter.color_extractor import ColorExtractor

extractor = ColorExtractor()
root = 'images/wrong_example/'
size = len(os.listdir('images/wrong_example'))
arr = []
for i in range(size):
    file = root + str(i) + '.jpg'
    col = extractor.get_color(file, 1)
    for j in col:
        arr.append(j[1])

df = pd.DataFrame(arr, index=None, columns=['R', 'G', 'B'])
df.to_csv('wrong.csv', index=False)
