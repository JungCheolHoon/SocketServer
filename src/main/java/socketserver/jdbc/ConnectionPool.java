package socketserver.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
public class ConnectionPool {
    private final HikariDataSource dataSource;
    public ConnectionPool() {
        var url = "jdbc:mysql://localhost/webchat?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true";
        var username = "webchat";
        var password = "webchat!";
        this.dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
    }
    public Connection get(){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
