package kr.co.mz.socketserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import kr.co.mz.socketserver.handler.RequestHandler;

public class SocketServerMain {
    public static void main(String[] args) throws IOException {
        // 서버 소켓 생성
        int port = 8080;

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("서버가 생성되었습니다.");
            while (true) {
                // 클라이언트 요청 대기
                Socket clientSocket = serverSocket.accept();
                // 클라이언트 요청 처리
                new RequestHandler().handleRequest(clientSocket);
            }
        }

    }// main
}// class
