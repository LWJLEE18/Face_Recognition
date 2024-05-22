import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
from firebase_admin import storage
from uuid import uuid4
import os
import cv2


cred = credentials.Certificate("/home/abc/test/af/py_key.json")
firebase_admin.initialize_app(cred,{
    'databaseURL' : 'https://academic-festival-default-rtdb.firebaseio.com/',
    'storageBucket' : 'academic-festival.appspot.com'})  
bucket = storage.bucket()  
ref = db.reference()  
newnameref = ref.child("newusername") 
newname = newnameref.get() 


blob = bucket.blob('newuser/'+newname+'.mp4') 

blob.download_to_filename('/home/abc/test/af/storage_newuser/'+newname+'.mp4') 
os.mkdir("/home/abc/test/af/dataset/"+newname)
def save_frames(video_path, dir_path, basename, ext='jpg', duration=4):
    # 동영상 캡쳐 객체 생성
    cap = cv2.VideoCapture(video_path)

    # 동영상 파일이 열리지 않으면 종료
    if not cap.isOpened():
        return

    # 저장할 디렉토리 생성
    os.makedirs(dir_path, exist_ok=True)
    base_path = os.path.join(dir_path, basename)

    # 전체 프레임 수 및 초당 프레임 수 계산
    total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
    frame_rate = int(total_frames / duration)

    # 프레임 수의 자릿수 계산
    digit = len(str(total_frames))

    # 초기화
    n = 0

    # 지정된 시간 동안 프레임 저장
    for _ in range(duration * frame_rate):
        ret, frame = cap.read()
        if ret:
            # 파일명에 순차적으로 프레임 번호 추가하여 이미지 저장
            cv2.imwrite('{}_{}.{}'.format(base_path, str(n).zfill(digit), ext), frame)
            n += 1
        else:
            break

    # 캡쳐 객체 해제
    cap.release() 


if __name__ == "__main__":  
    local_video_path = '/home/abc/test/af/storage_newuser/'+newname+'.mp4'
    # 저장할 프레임 이미지들의 디렉토리 경로
    dir_path = '/home/abc/test/af/dataset/'+newname
    # 저장할 이미지 파일들의 기본 이름
    basename = 'userimg'
    # 동영상에서 추출할 시간 (초)
    duration = 1

    save_frames(local_video_path, dir_path, basename, duration=duration) 
