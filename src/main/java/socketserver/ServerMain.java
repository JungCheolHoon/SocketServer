package socketserver;

import java.io.IOException;
import java.sql.SQLException;
import socketserver.server.Server;

public class ServerMain {

    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        // 서버 소켓 생성
        try (var server = new Server(SERVER_PORT)) {
            server.initialize();
            server.start();
        } catch (IOException ioe) {
            System.err.println("Failed To start Server:" + ioe.getMessage());
        } catch (SQLException sqle) {
            System.err.println("Failed to connect to Database: " + sqle.getMessage());
        }
    }
}
