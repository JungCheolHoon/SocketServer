package socketserver.server.response.dbcache;

import java.io.InputStream;
import java.sql.SQLException;
import socketserver.server.jdbc.JDBCConnection;

public class CachingPerformer {

    private final String path;
    private final JDBCConnection jdbcConnection;

    public CachingPerformer(String path, JDBCConnection jdbcConnection) {
        this.path = path;
        this.jdbcConnection = jdbcConnection;
    }

    public void insert(byte[] responseFileData) throws SQLException {
        var preparedStatement = jdbcConnection.get()
            .prepareStatement("insert into cache(uri,cache_data) values (?,?)");
        preparedStatement.setString(1, path);
        var blob = jdbcConnection.get().createBlob();
        blob.setBytes(1, responseFileData);
        preparedStatement.setBlob(2, blob);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public InputStream select() throws SQLException {
        var preparedStatement = jdbcConnection.get()
            .prepareStatement("select cache_data from cache where uri = ?");
        preparedStatement.setString(1, path);
        var resultSet = preparedStatement.executeQuery();
        InputStream cacheInputStream = null;
        while (resultSet.next()) {
            cacheInputStream = resultSet.getBlob(1).getBinaryStream();
        }
        resultSet.close();
        preparedStatement.close();
        return cacheInputStream;
    }
}
