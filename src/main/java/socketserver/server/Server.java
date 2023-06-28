package socketserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import socketserver.cache.Cache;
import socketserver.handle.ClientHandler;
import socketserver.jdbc.ConnectionPool;
import socketserver.session.Session;

public class Server implements AutoCloseable{
    private final ServerSocket socketServer;
    private final Cache cache;
    private final Session session;
    private final Connection connection;
    public Server(int port,Cache cache) {
        try {
            this.cache = cache;
            socketServer = new ServerSocket(port);
            session = new Session();
            connection = new ConnectionPool().get();
        } catch (IOException e) {
            System.err.println("An error occurred when creating the server socket: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void start(){
            while (true) {
                System.out.println("서버가 생성되었습니다.");
                try{
                    var clientHandler = new ClientHandler(socketServer.accept(),cache,session,connection);
                    clientHandler.handle();
                    clientHandler.close();
                } catch (IOException e) {
                    System.err.println("An error occurred when starting the server : " + e.getMessage());
                    throw new RuntimeException(e);
                }
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
