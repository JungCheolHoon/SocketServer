package socketserver.server.response.generate;

import java.io.File;
import java.util.UUID;
import socketserver.server.handle.ClientHandler;
import socketserver.server.session.Session;

public class HeaderGenerator {

    private final String path;
    private final Session session;
    private final String clientSessionId;

    public HeaderGenerator(String path, Session session, String clientSessionId) {
        this.path = path;
        this.session = session;
        this.clientSessionId = clientSessionId;
    }

    public StringBuilder generate() {
        var file = path.equals("/favicon.ico") ?
            new File(ClientHandler.PROJECT_DIRECTORY + "/resources/static" + path)
            : new File(ClientHandler.PROJECT_DIRECTORY + "/resources/templates" + path + ".html");
        var responseHeader = new StringBuilder();
        if (file.exists() && !file.isDirectory()) {
            responseHeader.append("HTTP/1.1 200 OK\r\n");
        } else {
            responseHeader.append("HTTP/1.1 404 Not Found\r\n");
        }
        if (path.equals("/favicon.ico")) {
            responseHeader.append("Content-Type: image/x-icon\r\n\r\n");
        } else {
            generateWhenHTML(responseHeader);
        }
        return responseHeader;
    }

    public void generateWhenHTML(StringBuilder responseHeader) {
        if (!session.containsSessionId(clientSessionId)) {
            var expirationTime = session.createSessionExpirationTime();
            var cookieName = UUID.randomUUID().toString();
            session.putSession(cookieName, expirationTime);
            responseHeader.append("Set-Cookie: JSESSIONID=").append(cookieName)
                .append("\r\n");
        } else if (session.sessionHasExpired(clientSessionId)) {
            responseHeader.append("Set-Cookie: JSESSIONID=").append(clientSessionId)
                .append("; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT")
                .append("\r\n");
        }
        responseHeader.append("Content-Type: text/html;").append("charset=UTF-8\r\n\r\n");
    }
}
