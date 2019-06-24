# Author : Hyunwoong
# When : 6/19/2019
# Homepage : github.com/gusdnd852

import operator

import cv2
import matplotlib.image as mpimg
import numpy as np
from matplotlib import pyplot as plt
from sklearn.cluster import KMeans


class ColorExtractor:
    def centroid_histogram(self, clt):
        numLabels = np.arange(0, len(np.unique(clt.labels_)) + 1)
        (hist, _) = np.histogram(clt.labels_, bins=numLabels)
        hist = hist.astype("float")
        hist /= hist.sum()
        return hist

    def plot_colors(self, hist, centroids):
        bar = np.zeros((50, 300, 3), dtype="uint8")
        startX = 0
        percent_arr = {}

        for (percent, color) in zip(hist, centroids):
            percent_arr[str(percent)] = color

        percent_arr = sorted(percent_arr.items(), key=operator.itemgetter(0))
        percent_arr.reverse()

        for (percent, color) in zip(hist, centroids):
            endX = startX + (percent * 300)
            cv2.rectangle(bar, (int(startX), 0), (int(endX), 50),
                          color.astype("uint8").tolist(), -1)
            startX = endX
        return bar, percent_arr

    def image_color_cluster(self, image_path, k=3):
        image = cv2.imread(image_path)
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        image = image.reshape((image.shape[0] * image.shape[1], 3))

        clt = KMeans(n_clusters=k)
        clt.fit(image)

        hist = self.centroid_histogram(clt)
        bar, p_arr = self.plot_colors(hist, clt.cluster_centers_)

        plt.figure()
        plt.axis("off")
        plt.imshow(bar)
        plt.show()
        return p_arr

    def get_color(self, file_name, k=3):
        image = mpimg.imread(file_name)
        col = self.image_color_cluster(file_name, k)
        return col
