# ServerSokcet 을 활용한 Web Server 구현
## Process
#### 1. Http Request URI Parsing
#### 2. Local 에 위치한 html 파일 디렉토리와 URL 매칭
#### 3. 매칭된 HTML 파일 Body (PayLoad) 로 설정
#### 4. Header (Status, Expriation Time) 생성 후 Http Response
#### 5. ClientHandler - ServiceController - HeaderGenerator - ResponeWriter 구조
#### 6. ServiceController 에서는 DB 접근하여 로그인 유무 확인시에만 사용하며, 라우팅 url 정보에 해당하는 정적인 페이지를 반환
#### 7. 동적인 페이지를 반환하는 경우는 없음
## Result
#### 1. 요청과 응답의 처리는 uri 를 통해 식별자와 라우팅 경로를 파싱해서 치라한다는 것을 알게 되었음
#### 2. Server 가 생성되는 전반적인 구조에 대해서 이해할 수 있었음
#### 2. HTTP 는 3가지 영역으로 구분되며, Start Line , Header , Body 영역으로 구분
#### 3. Start Line 은 최상단에 위치하며, 버전정보, 요청 메서드, uri, 상태코드 등에 대한 정보가 담김
