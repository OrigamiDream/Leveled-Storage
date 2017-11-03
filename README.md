# Leveled Storage
 
>레이어별로 데이터를 나눔으로서 좀더 효율적이고, 안정적인 <br/>
스토리지를 구성하도록 하는것을 목적으로 만들어진 프로젝트입니다.

******

# 목표
속도는 느리더라도 안정적인 데이터 보존을 중심으로,<br/>
로컬 파일 저장부터, 네트워크로 손쉽게 송·수신이 가능케 하는 것을 목표로 한 프로젝트입니다.

******

# 사용법
이 라이브러리의 기본적인 사용법은 [**이곳**](https://github.com/OrigamiDream/Leveled-Storage/blob/master/src/main/java/av/is/leveledstorage/example/ExampleStorage.java)에서 보실 수 있습니다.<br/>
가장 중요하게 사용되는 메소드들을 포함한 주요 내용만을 담았으며,<br/>
이외의 메소드와 필드의 사용은 **비권장**합니다.

또한 중요하게 사용되는 클래스마다 주석을 적어놓았으니, 참고하셔도 좋습니다.

******

# 이슈
이 라이브러리를 사용하는 과정에서 생기는 버그 (예: 스레드 데드록)을 발견하신다면,
오류 로그 또는 스레드덤프를 캡쳐하여 이슈에 올려주시길 바랍니다.

******

# 데이터 이동 개요도
![Transition](/TransitionPreview.png "Transition Preview")
