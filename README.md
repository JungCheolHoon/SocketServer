# ServerSokcet 을 활용한 Web Server Tutorial
## Process
#### 1. Http Request URI Parsing
#### 2. Local 에 위치한 html 파일 디렉토리와 URL 매칭
#### 3. 매칭된 HTML 파일 Body (PayLoad) 로 설정
#### 4. Header (Status, Expriation Time) 생성
#### 6  ResponseWriter 에서 파싱한 라우팅 경로에 해당하는 정적인 파일을 Http Body 에 write
#### 7. ClientHandler - ServiceController - HeaderGenerator - ResponeWriter 구조
#### 8. ServiceController 에서는 DB 접근하여 로그인 유무 확인시에만 사용하며, 라우팅 url 정보에 해당하는 정적인 페이지를 반환
#### 9. 동적인 페이지를 반환하는 경우는 없음
#### 10. Single Thread 기반
## Theory From Result
#### 1. Sring 프레임워크에서의 요청과 응답의 처리는 uri 를 통해 라우팅 경로로 파싱해서 처리
#### 2. Server 는 While문과 같은 반복된 구조로써 동작
#### 2. HTTP 는 3가지 영역으로 구분되며, Start Line , Header , Body 영역으로 구분
#### 3. Start Line 은 최상단에 위치하며, 버전정보, 요청 메서드, uri, 상태코드 등에 대한 정보를 가짐
#### 4. 동적인 페이지를 구현하는 경우에는 정적인 html 파일을 읽으면서 특정 키를 기준으로 데이터를 입력시켜주는 방식
