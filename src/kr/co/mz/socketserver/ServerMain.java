package kr.co.mz.socketserver;

import kr.co.mz.socketserver.cache.Cache;
import kr.co.mz.socketserver.server.Server;

public class ServerMain {
    private static final int SERVER_PORT = 8080;
    public static void main(String[] args){
        var cache = new Cache();
        // 서버 소켓 생성
        try(Server server = new Server(SERVER_PORT,cache)) {
            server.start();
        }
    }
}
