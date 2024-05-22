#! /usr/bin/python

# import the necessary packages
from picamera2 import Picamera2
import face_recognition
import imutils
import pickle
import time
import cv2
import requests 
import firebase_admin 
from firebase_admin import credentials  
from firebase_admin import db  
from firebase_admin import storage
from datetime import datetime 
from uuid import uuid4
import numpy as np



#새로운 사람이 식별될때만 작동하도록 'currentname'을 초기ㅗ하
currentname = "unknown"
#train_model.py 에서 생성된 pickle 파일모델에서 얼굴 결정
encodingsP = "/home/abc/test/af/encodings.pickle"
#하르 케스케이드 xml
cascade = "/home/abc/test/af/haarcascade_frontalface_default.xml"      
confidence_threshold = 0.4

cred = credentials.Certificate("/home/abc/test/af/py_key.json")
firebase_admin.initialize_app(cred,{
    'databaseURL' : 'https://academic-festival-default-rtdb.firebaseio.com/',
    'storageBucket' : 'academic-festival.appspot.com'})  
bucket = storage.bucket()


def discovered():   
    now = datetime.now()
    now_time = now.strftime('%Y%m%d%H%M%S')
    ref = db.reference() 
    filename = now_time+ '.jpeg'
    blob = bucket.blob('photo/'+ filename)     
    cv2.imwrite('/home/abc/test/af/Unknown/'+filename, frame)
    new_token = uuid4() 
    metadata = {"firebaseStorageDownloadTokens": new_token} 
    blob.metadata = metadata
    blob.upload_from_filename('/home/abc/test/af/Unknown/'+filename, content_type='image/jpeg')    
    ref.update({'Unknown' : 1})  
    ref.update({'Unknown_img' : now_time}) 
    time.sleep(3)

# haar와 함께 학습된 얼굴과 임베딩을 로드
# 얼굴 감지를 위해 케스케이드 사용
print("[INFO] loading encodings + face detector...")
data = pickle.loads(open(encodingsP, "rb").read())
detector = cv2.CascadeClassifier(cascade)

# 비디오 스트림 초기화 밑 실행
print("[INFO] starting video stream...")
picam2 = Picamera2()
picam2.preview_configuration.main.size = (1280, 720)
picam2.preview_configuration.main.format = "RGB888"
picam2.preview_configuration.align()
picam2.configure("preview")
picam2.start()
# vs = VideoStream(usePiCamera=True).start()


# 프레임

# 비디오파일스트림 루프
try:
    while True:
        # 비디오 스트림에서 프레임을 가져와 크기 조정
        # 500px로 설정(처리속도 최적화)
        frame = picam2.capture_array()
        
        # (1) BGR에서 회색조(얼굴 인식용)로 입력 프레임을 변환하고 (2) BGR에서 RGB(얼굴 인식용)로 변환
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

        # grayscale 프레임에서 얼굴을 탐지
        rects = detector.detectMultiScale(gray, scaleFactor=1.1, 
            minNeighbors=5, minSize=(30, 30),
            flags=cv2.CASCADE_SCALE_IMAGE)

        # OpenCV는 (x, y, w, h) 순서대로 경계 상자 좌표를 반환
        # 필요한 순서는 (위,오른쪽,아래,왼쪽)순이기때문에 재설계
        boxes = [(y, x + w, y + h, x) for (x, y, w, h) in rects]

        # face bounding box에 대한 얼굴 임베딩을 계산
        encodings = face_recognition.face_encodings(rgb, boxes)
        names = []

        # facial 임베딩 루프
        for encoding in encodings:
            # 학습된 얼굴과 캠의 얼굴과 동일한지를 알아냄 없으면 unknow로 출력하도록 설정
            matches = face_recognition.compare_faces(data["encodings"], encoding)
            name = "Unknown"

            # 동일한 얼굴을 찾았는지 확인
            if True in matches:
                # 일치하는 모든 인덱스를 찾은다음 딕셔너리를 초기화 하여 일치하는 총횟수를 계산
                face_distances = face_recognition.face_distance(data["encodings"], encoding)
                best_match_index = np.argmin(face_distances)

                # 일치하는 인덱스를 반복하고 인식된 얼굴에 대한 카운트를 저장
                if matches[best_match_index] and face_distances[best_match_index] < confidence_threshold:
                    name = data["names"][best_match_index]

                #데이터셋에서 누군가 식별되면 이름을 출력
                if currentname != name:
                    currentname = name
                    print(currentname) 
                    ref = db.reference()
                    ref.update({'app_Btn' : 1})
                    #discovered()
            # 이름을 업데이트 함
            names.append(name) 

            if False in matches: 
                discovered()  
                print(name)
    
        # 인식된 얼굴을 반복
        for ((top, right, bottom, left), name) in zip(boxes, names):
            # 이미지에 예측된 얼굴을 그림 색상을 BGR
            cv2.rectangle(frame, (left, top), (right, bottom),
                (0, 255, 225), 2)
            y = top - 15 if top - 15 > 15 else top + 15
            cv2.putText(frame, name, (left, y), cv2.FONT_HERSHEY_SIMPLEX,
                .8, (0, 255, 255), 2)

        # 화면에 이미지를 표시
        cv2.imshow("Facial Recognition is Running", frame)
        key = cv2.waitKey(1) & 0xFF

        # 'q'를 누르면 루프에서 빠져나감
        if key == ord("q"):
            break

finally:
    # Release resources
    cv2.destroyAllWindows()
    picam2.stop()
    picam2.close()
