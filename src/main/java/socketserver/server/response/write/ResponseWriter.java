package socketserver.server.response.write;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import socketserver.server.handle.ClientHandler;
import socketserver.server.jdbc.JDBCConnection;
import socketserver.server.response.dbcache.CachingPerformer;

public class ResponseWriter {

    private final StringBuilder responseHeader;
    private final OutputStream socketOutputStream;
    private final String path;
    private final byte[] buffer;
    private int readCount;

    public ResponseWriter(StringBuilder httpResponseBuilder, OutputStream socketOutputStream,
        String uri) {
        this.responseHeader = httpResponseBuilder;
        this.socketOutputStream = socketOutputStream;
        this.path = uri;
        this.buffer = new byte[4096];
        this.readCount = -1;
    }

    public void write(JDBCConnection jdbcConnection) throws IOException, SQLException {
        socketOutputStream.write(responseHeader.toString().getBytes());
        var file =
            path.equals("/favicon.ico") ?
                new File(ClientHandler.PROJECT_DIRECTORY + "/resources" + path)
                : new File(ClientHandler.PROJECT_DIRECTORY + "/resources/templates" + path + ".html");
        InputStream cacheInputStream = new CachingPerformer(path, jdbcConnection).select();
        if (cacheInputStream != null) {
            while ((readCount = cacheInputStream.read(buffer)) != -1) {
                socketOutputStream.write(buffer, 0, readCount);
            }
        } else {
            var fileInputStream =
                file.exists() && !file.isDirectory() ?
                    new FileInputStream(file)
                    : new FileInputStream(
                        ClientHandler.PROJECT_DIRECTORY + "/resources/templates/exception/404.html");
            if (file.exists() || !path.equals("/favicon")) {
                writeFile(jdbcConnection, fileInputStream);
            }
        }
    }

    public void writeFile(JDBCConnection jdbcConnection, InputStream fileInputStream) throws SQLException, IOException {
        var arrayOutputStream = new ByteArrayOutputStream();
        while ((readCount = fileInputStream.read(buffer)) != -1) {
            socketOutputStream.write(buffer, 0, readCount);
            arrayOutputStream.write(buffer, 0, readCount);
        }
        fileInputStream.close();
        new CachingPerformer(path, jdbcConnection).insert(arrayOutputStream.toByteArray());
        arrayOutputStream.close();
    }
}
