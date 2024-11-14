# Java TCP/HTTP Server Implementation

Java의 기본 네트워킹 기능을 사용하여 TCP 기반의 HTTP 웹 서버를 구현한 프로젝트입니다. TCP ServerSocket을 기반으로 하여 HTTP 프로토콜을 구현하였으며, Spring Framework 없이 순수 Java로 작성되었습니다.

## 📋 목차
- [프로젝트 개요](#프로젝트-개요)
- [주요 기능](#주요-기능)
- [시스템 아키텍처](#시스템-아키텍처)
- [기술 상세](#기술-상세)
- [실행 방법](#실행-방법)
- [학습 내용](#학습-내용)

## 프로젝트 개요
이 프로젝트는 네트워크 프로토콜의 동작 원리를 이해하기 위해 TCP/IP 스택의 여러 계층을 직접 다루어봅니다:
- **전송 계층**: Java ServerSocket을 사용한 TCP 통신
- **애플리케이션 계층**: TCP 연결 위에서 동작하는 HTTP 프로토콜 구현

## 주요 기능
- TCP 연결 관리 (연결 수립, 데이터 전송, 연결 종료)
- HTTP 프로토콜 구현
  - HTTP 요청 수신 및 파싱
  - HTTP 응답 생성 및 전송
- 정적 리소스(HTML, CSS, JS 등) 제공
- 간단한 인증 시스템 (로그인 기능)
- Single Thread 기반의 요청 처리

## 시스템 아키텍처
### 핵심 컴포넌트
1. **TCP Server (ServerSocket)**
   - 포트 바인딩 및 TCP 연결 수신
   - 클라이언트 소켓 연결 관리
   - 지속적인 연결 대기 (While loop)

2. **ClientHandler**
   - TCP 소켓 스트림 처리
   - HTTP 프로토콜 해석
   - 바이트 스트림을 HTTP 메시지로 변환

3. **HeaderGenerator**
   - HTTP 헤더 생성
   - 상태 코드 및 만료 시간 설정

4. **ResponseWriter**
   - HTTP 응답 메시지 생성
   - 정적 리소스 로딩 및 전송
   - 바이트 스트림 변환 및 전송

5. **ServiceController**
   - 비즈니스 로직 처리
   - DB 연동 (로그인 검증)

## 기술 상세
### TCP/HTTP 통신 구조
1. **TCP 연결 처리**
   - 소켓 연결 수립 (3-way handshake)
   - 입출력 스트림 관리
   - 연결 종료 처리

2. **HTTP 메시지 구조**
   - Start Line
     - HTTP 버전
     - 요청 메서드 (GET, POST 등)
     - URI/URL
     - 상태 코드
   - Header
     - 메타데이터
     - 컨텐츠 타입
     - 만료 시간
   - Body
     - 실제 전송될 데이터
     - HTML 컨텐츠
     - 폼 데이터 등

### URL 처리
- URI vs URL 개념 구분
  - URI: 리소스 식별자 (도메인)
  - URL: 리소스 위치 정보 포함 (도메인 + 경로)
- 요청 URL 파싱 및 로컬 리소스 매핑
- 정적/동적 컨텐츠 구분 처리

## 실행 방법
```bash
# 프로젝트 실행
$ java -jar webserver.jar

# 기본 포트: 8080
# 접속 방법: http://localhost:8080
```

## 학습 내용
### 1. TCP/IP 네트워크 스택
- 전송 계층 (TCP) 동작 원리
- 소켓 프로그래밍 기초

### 2. HTTP 프로토콜 구현
- HTTP 메시지 구조 이해
- 요청-응답 생명주기
- 헤더 및 바디 처리

### 3. 웹 서버 아키텍처
- Single Thread 모델의 특징
- 정적/동적 컨텐츠 처리
- 리소스 관리 및 최적화
