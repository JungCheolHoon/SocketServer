package socketserver.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class JDBCConnection {
    private final Connection connection;
    public JDBCConnection() {
        var url = "jdbc:mysql://localhost/webchat?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true";
        var username = "webchat";
        var password = "webchat!";
        try {
            connection = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Connection get(){
        return connection;
    }
    public void close(PreparedStatement preparedStatement , ResultSet resultset){
        try {
            resultset.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
