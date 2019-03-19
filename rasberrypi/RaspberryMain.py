import cv2
import time
import picamera
import json
import sys
import threading
import bluetooth_server as BS
import FaceCongnitive

font = cv2.FONT_ITALIC


def blueTooth():
    BS.start()

#BS.start()


#face_thread = threading.Thread(target=faceDetect)
#face_thread = threading.Thread(target=faceDetect,args=(emotion_judge,emotion_late))

#face_thread.start()
#face_thread.start()