import time
import picamera
import cognitive_face as CF
import threading
import Command

KEY = 'b6889d34f1184378bfc045cd6b8a92a9'  #a valid subscription key (keeping the quotes in place).
CF.Key.set(KEY)
BASE_URL = 'https://koreacentral.api.cognitive.microsoft.com/face/v1.0/'  # Replace with your regional Base URL
CF.BaseUrl.set(BASE_URL)
lock = False

def FaceCheck(img_url, late):
    #img_url ="a.jpg"  
   
    while(True):
     try:
        time.sleep(1)
      
        faces = CF.face.detect(img_url, face_id=True, landmarks=False, attributes='emotion')
        emotion_judge ="None"
        emotion_late =0
   
    
        for face in faces:
            print(face['faceRectangle'])
            for emotion in face['faceAttributes']['emotion']:
            
                 
                if emotion_late< float(face['faceAttributes']['emotion'][emotion]):
                    emotion_judge = emotion
                    emotion_late = float(face['faceAttributes']['emotion'][emotion])
                    
        print(emotion_judge)
        Command.SendEmotion(emotion_judge)
     except:
       pass



def face_check(url):
    face_thread = threading.Thread(target=FaceCheck, args=(url, 1))
    face_thread.daemon = True
    face_thread.start()
    
    
    
    
#face_check("face.jpg")