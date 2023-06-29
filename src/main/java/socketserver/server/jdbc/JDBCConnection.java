package socketserver.server.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCConnection {

    private final Connection connection;

    public JDBCConnection() throws SQLException {
        var url = "jdbc:mysql://localhost/webchat?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true";
        var username = "webchat";
        var password = "webchat!";
        connection = DriverManager.getConnection(url, username, password);
    }

    public Connection get() {
        return connection;
    }

    public void close() throws SQLException {
        this.connection.close();
    }

    public void close(PreparedStatement preparedStatement) {
        try {
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(PreparedStatement preparedStatement, ResultSet resultset) {
        try {
            resultset.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
