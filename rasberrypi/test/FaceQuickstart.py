import cognitive_face as CF
import json

KEY = 'b6889d34f1184378bfc045cd6b8a92a9'  # Repb6889d34f1184378bfc045cd6b8a92a9lace with a valid subscription key (keeping the quotes in place).
CF.Key.set(KEY)

BASE_URL = 'https://koreacentral.api.cognitive.microsoft.com/face/v1.0/'  # Replace with your regional Base URL
CF.BaseUrl.set(BASE_URL)

# You can use this example JPG or replace the URL below with your own URL to a JPEG image.
img_url = './messigray.jpg'
faces = CF.face.detect(img_url, face_id=True, landmarks=False, attributes='emotion')




emotion_late=0
emotion_judge ="Sorry"
print(faces)
for face in faces:
        for emotion in face['faceAttributes']['emotion']:
                print(emotion,face['faceAttributes']['emotion'][emotion])
                if emotion_late< float(face['faceAttributes']['emotion'][emotion]):
                        emotion_judge =emotion
                        emotion_late = float(face['faceAttributes']['emotion'][emotion])
                



print(emotion_judge)
