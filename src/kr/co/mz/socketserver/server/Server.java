package kr.co.mz.socketserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import kr.co.mz.socketserver.cache.Cache;
import kr.co.mz.socketserver.cookie.Cookie;
import kr.co.mz.socketserver.request.handler.RequestHandler;

public class Server implements AutoCloseable{

    private final ServerSocket socketServer;

    public Server(int port) {
        try {
            socketServer = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("An error occurred when creating the server socket: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void start(){
        try {
            Cache cache = new Cache();
            Cookie cookie = new Cookie();
            while (true) {
                System.out.println("서버가 생성되었습니다.");
                // 클라이언트 요청 대기
                Socket clientSocket = socketServer.accept();
                // 클라이언트 요청 처리
                new RequestHandler().handleRequest(clientSocket,cache,cookie);
            }
        } catch (IOException e) {
            System.err.println("An error occurred when starting the server : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void close(){
        try {
            socketServer.close();
        } catch (IOException e) {
            System.err.println("An error occurred when closing the server : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
