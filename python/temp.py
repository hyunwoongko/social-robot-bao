# Author : Hyunwoong
# When : 5/30/2019
# Homepage : github.com/gusdnd852
import os

from painter.stylize import Stylizer

root = 'painter/images/output'

for i in os.listdir(root):
    Stylizer(i).stylize(True)
