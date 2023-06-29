package socketserver.server.handle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import socketserver.server.jdbc.JDBCConnection;
import socketserver.server.request.parse.RequestParser;
import socketserver.server.response.generate.HeaderGenerator;
import socketserver.server.response.write.ResponseWriter;
import socketserver.server.session.Session;

public class ClientHandler {

    public static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    private final Socket clientSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final Session session;
    private final JDBCConnection jdbcConnection;

    public ClientHandler(Socket clientSocket, Session session, JDBCConnection jdbcConnection)
        throws IOException {
        this.clientSocket = clientSocket;
        this.session = session;
        this.jdbcConnection = jdbcConnection;
        this.inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();
    }

    public void handle() throws IOException, SQLException {
        var requestParser = new RequestParser(inputStream);
        var urn = requestParser.parseToURN();
        if (urn.getPath() == null) {
            return;
        }
        var path = urn.getPath().equals("/") ? "/main"
            : urn.getPath();
        var clientSessionId = requestParser.parseToSessionId();
        var responseHeader = new HeaderGenerator(path, session, clientSessionId).generate();
        new ResponseWriter(responseHeader, outputStream, path).write(
            jdbcConnection);
    }

    public void close() throws IOException {
        clientSocket.close();
    }
}
