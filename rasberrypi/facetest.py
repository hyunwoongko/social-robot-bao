## -*- coding: utf-8 -*-  # 한글 주석쓸려면 적기
import cv2
import time
import picamera
import cognitive_face as CF
import json


KEY = 'b6889d34f1184378bfc045cd6b8a92a9'  # Repb6889d34f1184378bfc045cd6b8a92a9lace with a valid subscription key (keeping the quotes in place).
CF.Key.set(KEY)
BASE_URL = 'https://koreacentral.api.cognitive.microsoft.com/face/v1.0/'  # Replace with your regional Base URL
CF.BaseUrl.set(BASE_URL)
img_url = 'facecapture.jpg'
font = cv2.FONT_ITALIC



 
 
def faceDetect():
    emotion_judge =""
    emotion_late = 0
    eye_detect = True
    face_cascade = cv2.CascadeClassifier("./haarcascade_frontalface_default.xml")  # 얼굴찾기 haar 파일
    eye_cascade = cv2.CascadeClassifier("./haarcascade_eye.xml") # 눈찾기 haar 파일
 
    try:
        cam = cv2.VideoCapture(-1)
    except:
        print("camera loading error")
        return
 
    while True:
        ret, frame = cam.read()
        if not ret:
            break
 
        if eye_detect:
            info = "Eye Detention ON"
        else:
            info = "Eye Detection OFF"
 
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        faces = face_cascade.detectMultiScale(gray,1.3, 5)
 
        faces = face_cascade.detectMultiScale(gray, 1.3, 5)
 
        #카메라 영상 왼쪽위에 위에 셋팅된 info 의 내용 출력
        cv2.putText(frame, emotion_judge, (5,15), font, 0.5, (255,0, 255),1)
 
        for(x,y, w,h) in faces:
            cv2.rectangle(frame, (x,y), (x+w, y+h), (255,0,0), 2)  #사각형 범위
            cv2.putText(frame, "Detected Face", (x-5, y-5), font, 0.5, (255,255,0),2)  #얼굴찾았다는 메시지
            if eye_detect:  #눈찾기
                roi_gray = gray[y:y+h, x:x+w]
                roi_color = frame[y:y+h, x:x+w]
                eyes = eye_cascade.detectMultiScale(roi_gray)
                for (ex,ey,ew,eh) in eyes:
                    cv2.rectangle(roi_color, (ex, ey), (ex+ew, ey+eh), (0,255,0), 2)
 
        cv2.imshow("frame", frame)
        k=cv2.waitKey(30)
 
        #실행 중 키보드 i 를 누르면 눈찾기를 on, off한다.
        if k == ord('i'):
            cv2.imwrite('facecapture.jpg',frame)
            
            faces = CF.face.detect(img_url, face_id=True, landmarks=False, attributes='emotion')
            emotion_judge =""
            emotion_late =0
            for face in faces:
                    for emotion in face['faceAttributes']['emotion']:
                        print(emotion,face['faceAttributes']['emotion'][emotion])
                        if emotion_late< float(face['faceAttributes']['emotion'][emotion]):
                            emotion_judge = emotion
                            emotion_late = float(face['faceAttributes']['emotion'][emotion])
        if k == 27:
            break
    cam.release()
    cv2.destroyAllWindows()
 
faceDetect()


def faceEmotion():

        faces = CF.face.detect(img_url, face_id=True, landmarks=False, attributes='emotion')
        for face in faces:
                for emotion in face['faceAttributes']['emotion']:
                    if emotion_late< float(face['faceAttributes']['emotion'][emotion]):
                        print(emotion,face['faceAttributes']['emotion'][emotion])
                        emotion_judge =emotion
                        emotion_late = float(face['faceAttributes']['emotion'][emotion])
  

