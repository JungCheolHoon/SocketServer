package socketserver.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBExecution {

    private final Connection connection;

    public DBExecution(Connection connection) throws SQLException {
        this.connection = connection;
    }

    public PreparedStatement get(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    void close(PreparedStatement preparedStatement) {
        try {
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
