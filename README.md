# TCP Server - Web Server Tutorial<br/>


## Process
#### 1. ServerSokcet 을 활용한 서버 구현
#### 2. ClientHandler - HeaderGenerator - ResponeWriter 구조로써 Single Thread 기반
#### 3. HTTP 통신 규격으로 온 요청의 StartLine 에서 URL 정보를 Parsing
#### 4. Local 에 위치한 html 파일 디렉토리와 리소스 위치 정보를 매칭
#### 5. Header (Status, Expriation Time) 생성
#### 6. 해당 경로의 리소스(HTML) 파일을 HTTP Body (PayLoad) 에 Write
#### 7. ServiceController 에서는 DB 접근하여 로그인 유무 확인시에만 사용하며, 항상 정적인 페이지를 반환<br/><br/><br/>


## Realization
#### 1. Sring 프레임워크에서의 요청과 응답의 처리는 URL을 리소스 위치 정보로 파싱해서 처리
#### 2. Server 는 While문과 같은 반복된 구조로써 동작
#### 2. HTTP 는 3가지 영역으로 구분되며, Start Line , Header , Body 영역으로 구분
#### 3. Start Line 은 최상단에 위치하며, 버전정보, 요청 메서드, uri, 상태코드 등에 대한 정보를 가짐
#### 4. 동적인 페이지를 구현하는 경우에는 정적인 html 파일을 읽으면서 특정 키를 기준으로 데이터를 교체시켜주는 방식
#### 6. URI 는 도메인 , URL 은 도메인(URI)에 리소스 위치가 포함되어있음
