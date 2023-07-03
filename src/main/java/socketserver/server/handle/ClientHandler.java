package socketserver.server.handle;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import socketserver.server.DBExecution;
import socketserver.server.request.parse.RequestParser;
import socketserver.server.response.generate.HeaderGenerator;
import socketserver.server.response.write.ResponseWriter;
import socketserver.server.session.Session;

public class ClientHandler {

    public static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    private final Socket clientSocket;
    private final DBExecution dbExecution;
    private final Session session;

    public ClientHandler(Socket clientSocket,
        DBExecution dbExecution, Session session) throws IOException {
        this.clientSocket = clientSocket;
        this.dbExecution = dbExecution;
        this.session = session;
    }

    public void handle() throws IOException, SQLException {
        var requestParser = new RequestParser(clientSocket.getInputStream());
        var path = requestParser.getPath();
        if (path.isEmpty()) {
            return;
        }
        path = path.equals("/") ? "/main" : path;
        var clientSessionId = requestParser.parseToSessionId();
        var responseHeader = new HeaderGenerator(path, session, clientSessionId).generate();
        new ResponseWriter(responseHeader, clientSocket.getOutputStream(), path).write(
            dbExecution);
    }

    public void close() throws IOException {
        clientSocket.close();
    }
}
