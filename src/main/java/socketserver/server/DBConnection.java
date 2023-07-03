package socketserver.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private final Connection connection;

    public DBConnection() throws SQLException {
        final var url = "jdbc:mysql://localhost/webchat?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true";
        final var username = "webchat";
        final var password = "webchat!";
        connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);
    }

    Connection get() {
        return connection;
    }

    void close() throws SQLException {
        this.connection.close();
    }
}
