import cv2
import time
import picamera
import cognitive_face as CF
import json
import bluetooth_server as BS       
       
       
COMMAND=""

class CommandListner:
    _instance = None

    @classmethod
    def _getInstance(cls):
        return cls._instance

    @classmethod
    def instance(cls, *args, **kargs):
        cls._instance = cls(*args, **kargs)
        cls.instance = cls._getInstance
        return cls._instance

    def:setCommand(command)
        COMMAND =command
         
    def:Command()  
       
        if  BS.command == COMMAND :
            #faceEmotion(emotion_judge,emotion_late)
            print(BS.command)
            cv2.imwrite('facecapture.jpg',frame)
            faces = CF.face.detect(img_url, face_id=True, landmarks=False, attributes='emotion')
            emotion_judge =""
            emotion_late =0
            BS.command=""
            for face in faces:
                 for emotion in face['faceAttributes']['emotion']:
                     print(emotion,face['faceAttributes']['emotion'][emotion])
                     
                     if emotion_late< float(face['faceAttributes']['emotion'][emotion]):
                         emotion_judge = emotion
                         emotion_late = float(face['faceAttributes']['emotion'][emotion])
                         
                         
                         
                         