package kr.co.mz.socketserver;

import kr.co.mz.socketserver.server.Server;

public class ServerMain {
    private static final int PORT = 8080;
    public static void main(String[] args){
        // 서버 소켓 생성
        try(Server server = new Server(PORT)) {
            server.start();
        }

    }// main
}// class
