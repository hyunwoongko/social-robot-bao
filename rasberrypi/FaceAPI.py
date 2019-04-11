import json
import uuid

import cognitive_face as CF
import threading
import util
import unittest

KEY = 'b6889d34f1184378bfc045cd6b8a92a9'  #a valid subscription key (keeping the quotes in place).
CF.Key.set(KEY)
BASE_URL = 'https://koreacentral.api.cognitive.microsoft.com/face/v1.0/'  # Replace with your regional Base URL
CF.BaseUrl.set(BASE_URL)
lock = False

def FaceCheck(img_url, late):
    #img_url ="a.jpg"
    person_groupid=str("ef0f6a16-5b58-11e9-a18c-b827eb3661ab")
    while(True):
        try:


            faces = CF.face.detect(img_url, face_id=True, landmarks=False, attributes='emotion')
            emotion_judge ="None"
            emotion_late =0
            faceID={}

            for face in faces:
                print(face['faceRectangle'])
                #Command.SendEmotion("direct:"+face['faceRectangle'])
                print(face["faceId"])
                faceID[0]=face["faceId"]
                for emotion in face['faceAttributes']['emotion']:


                    if emotion_late< float(face['faceAttributes']['emotion'][emotion]):
                        emotion_judge = emotion
                        emotion_late = float(face['faceAttributes']['emotion'][emotion])


            print("emotion:"+emotion_judge)
            #Command.SendEmotion("emotion:"+emotion_judge)

        except Exception as ex:
            print(ex)
            #pass



def face_check(url):
    face_thread = threading.Thread(target=FaceCheck, args=(url, 1))
    face_thread.daemon = True
    face_thread.start()



def set_Face():
    img_url="face.jpg"
    img_url2="dica.jpg"
    img_url3="foo2.jpg"
    name='wonwoo'
    person_group_id= str(uuid.uuid1())
    faceID=[]
    person_id={}
    persisted_face_id={}
    print(person_group_id)

    CF.person_group.create(person_group_id,name,user_data="펄슨 그룹 테스팅")
    faces = CF.face.detect(img_url2, face_id=True, landmarks=False, attributes='emotion')
    res = CF.person.create(person_group_id, '원우',user_data='원우')

    util.wait()
    print("creatPerson: ",res)
    person_id[0] = res['personId']
    person_id[1]= CF.person.create(person_group_id, '디카프리오',user_data="디카프리오")['personId']

    print(person_id[0])
    print(res)

    res = CF.person.add_face(img_url, person_group_id,
                             person_id[0],user_data="wonwoo")
    persisted_face_id[0] = res['persistedFaceId']
    res = CF.person.add_face(img_url2, person_group_id,
                             person_id[1],user_data="디카프리오")

    persisted_face_id[1] = res['persistedFaceId']
    res = CF.person.update_face(person_group_id,
                                person_id[0],
                                persisted_face_id[0], 'TempUserData')
    res = CF.person.update_face(person_group_id,
                                person_id[1],
                                persisted_face_id[1], 'TempUserData')


    print(res)
    res = CF.person_group.train(person_group_id)
    util.wait()
    res = CF.person_group.update(person_group_id, 'wonwoo')




    for face in faces:
        print(face)
        print(face["faceId"])
        faceID.append(face['faceId'])
        print(type(faces))

    res = CF.face.identify(
    faceID,

    person_group_id=person_group_id)
    print(res)


    try:
        for identify in res:
         for candidates in identify["candidates"]:
             person= CF.person.get(person_group_id,candidates['personId'])
             print(person["name"])

        #res=CF.person.get(person_group_id,person_id[1])
        #print(res)
        CF.person_group.delete(person_group_id)
    except Exception as ex:
        print(ex)
        CF.person_group.delete(person_group_id)





#face_check("face.jpg")
#time.sleep(0.5)
#face_check("face.jpg")
set_Face()
"""
def identify(face_ids,
             person_group_id=None,
             large_person_group_id=None,
             max_candidates_return=1,
             threshold=None):
"""