## 얼굴인식 스마트도어락

사용장치 
- Raspberry Pi4
- Picamera Module V3
- 삼성 SHS-1321 디지털도어락

사용 언어 
- Python 
- Java

개발 환경 
- VScode 
- Android Studio
- FireBase

요약  
- Raspberry Pi4에 부착된 Picamera Module V3을 통해 얼굴인식으로 도어락의 잠금 및 열림을 제어
- 제작한 Application으로 도어락 원격 제어 가능
- Unknown이 감지되면 Application을 통하여 알림
- 도어락의 동작(열림 및 닫힘)에 따라 Firebase에서 로그 생성후 이는 Application을 통해 확인가능
- 얼굴 인식의 데이터 객체는 파이썬의 pickle을 활용하여 저장
- 새로운 사람을 추가할경우에는 동영상을 업로드하면 영상을 프레임단위로 쪼개서 사진으로 변환시킨후 이를 학습
- Object Detection이 가능한 머신러닝 기반 Object Detection 알고리즘 Haar Cascade를 사용

전체 구성도


<img src="https://github.com/lwonj/Face_Recognition/assets/120168925/57d61612-8084-4232-8759-3283f5de13c1"  width="800" height="300"/>**)

구현화면
