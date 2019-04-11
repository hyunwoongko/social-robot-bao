import time
import cognitive_face as CF
import json
#import bluetooth_server as BS
import cv2
import threading
import picamera
import os
import subprocess
import select
import serial
import time
import json
import re
from functools import singledispatch
import Wifi_Configer as WIFI

KEY = 'b6889d34f1184378bfc045cd6b8a92a9'  #a valid subscription key (keeping the quotes in place).
CF.Key.set(KEY)
BASE_URL = 'https://koreacentral.api.cognitive.microsoft.com/face/v1.0/'  # Replace with your regional Base URL
CF.BaseUrl.set(BASE_URL)
img_url = 'facecapture.jpg'

#camera = PiCamera()
Cam = None

def InitFrame(Frame):
    global Cam
   
    Cam =Frame

def EmotionCheck():
    #global carmera
    global Cam
    emotion_judge =""
    emotion_late = 0
    
    #camera.start_preview()
    #camera.capture('facecapture.jpg')
    #camera.stop_preview()
    
    #frame = Cam.read()
    cv2.imwrite('facecapture.jpg',Cam)
    #picamera.PiCamera().capture('image.jpg')
    faces = CF.face.detect(img_url, face_id=True, landmarks=False, attributes='emotion')
    emotion_judge ="None"
    emotion_late =0
    for face in faces:
        for emotion in face['faceAttributes']['emotion']:
            print(emotion,face['faceAttributes']['emotion'][emotion])
                     
            if emotion_late< float(face['faceAttributes']['emotion'][emotion]):
               emotion_judge = emotion
               emotion_late = float(face['faceAttributes']['emotion'][emotion])
                
    print(emotion_judge)
    serialSend=SerialComm.instance()
    serialSend.send_serial(emotion_judge)
    

def SendEmotion(emotion=""):
    SerialComm.instance().send_serial(emotion)

def Commands(command=""):
    print(command)

    #from bluetooth_server import SerialComm
    #serialComm = SerialComm().instance()
   
    
 
    if command == "emotion" :
        #faceEmotion(emotion_judge,emotion_late)
       #EmotionCheck()
        print("")
       # SerialComm.instance().send_serial("Signal~~~"+"l")
    if command == "Dance" :   
      SerialComm.instance().send_serial("gooood~"+"l")
      
def JsonCommands(SSID,PASS):
    interface_name = "wlan0" # i. e wlp2s0
    print(SSID,PASS)
    
    F =WIFI.Finder(server_name=SSID,
            password=PASS,
            interface=interface_name)
    F.run()
    #from bluetooth_server import SerialComm
    #serialComm = SerialComm().instance()
   

       
       

      
def StartCommand(command=""):
    server_thread = threading.Thread(target=Commands,args=(command,))
    server_thread.daemon = True
    server_thread.start()        
                
                    
def JsonStartCommand(command):
    print("(json)",command)#wifi connect
    ssid = command['SSID']
    password = command['PWD']
    print(type(ssid),ssid)
    print(type(password),password)
    
    server_thread = threading.Thread(target=JsonCommands,args=(ssid,password))
    server_thread.daemon = True
    server_thread.start()   
                      
                      
class SerialComm:
    
    @classmethod
    def __getInstance(cls):
        return cls.__instance

    @classmethod
    def instance(cls, *args, **kargs):
        cls.__instance = cls(*args, **kargs)
        cls.instance = cls.__getInstance
        return cls.__instance

    def __init__(self):
        self.port = serial.Serial("/dev/rfcomm0", baudrate=9600, timeout=1)

    def send_serial(self, text):
        stringTest= ""
        serial=None
        stringTest=text+"l"
        bytecode = stringTest.encode("utf-8")
        self.port.write(bytecode)