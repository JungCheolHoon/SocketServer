package socketserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import socketserver.server.handle.ClientHandler;
import socketserver.server.jdbc.JDBCConnection;
import socketserver.server.session.Session;

public class Server implements AutoCloseable {

    private final int port;
    private final Session session;
    private final JDBCConnection jdbcConnection;
    private ServerSocket socketServer;

    private volatile boolean isServerRunning = true;

    public Server(int port) throws SQLException {
        this.port = port;
        this.session = new Session();
        this.jdbcConnection = new JDBCConnection();
        System.out.println("서버가 생성되었습니다.");
    }

    public void initialize() throws IOException {
        System.out.println("서버가 초기화 되었습니다.");
        socketServer = new ServerSocket(port);
    }

    public void start() {
        while (isServerRunning) {
            System.out.println("서버가 시작되었습니다.");
            try {
                var clientHandler = new ClientHandler(
                    socketServer.accept(), session, jdbcConnection
                );
                clientHandler.handle();
                clientHandler.close();
            } catch (IOException | SQLException e) {
                System.err.println(
                    "An error occurred when starting the server : " + e.getMessage());
            }
        }
    }

    @Override
    public void close() {
        System.out.println("서버가 종료됩니다.");
        isServerRunning = false;
        try {
            socketServer.close();
        } catch (IOException e) {
            System.err.println("An error occurred when closing the server : " + e.getMessage());
        }

        try {
            jdbcConnection.close();
        } catch (SQLException sqle) {
            System.err.println("Failed to close Connection:" + sqle.getMessage());
        }
        System.out.println("서버가 종료되었습니다.");
    }
}
