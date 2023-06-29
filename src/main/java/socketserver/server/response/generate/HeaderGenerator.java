package socketserver.server.response.generate;

import java.io.File;
import java.util.UUID;
import socketserver.server.handle.ClientHandler;
import socketserver.server.session.Session;

public class HeaderGenerator {

    private final File file;
    private final String path;
    private final Session session;
    private final String clientCookie;

    public HeaderGenerator(String uri, Session session, String clientCookie) {
        this.path = uri;
        this.session = session;
        this.clientCookie = clientCookie;
        this.file = uri.equals("/favicon.ico") ? new File(
            ClientHandler.PROJECT_DIRECTORY + "/resources" + uri)
            : new File(ClientHandler.PROJECT_DIRECTORY + "/resources/templates" + uri + ".html");
    }

    public StringBuilder generate() {
        var responseHeader = new StringBuilder();
        if (file.exists() && !file.isDirectory()) {
            responseHeader.append("HTTP/1.1 200 OK\r\n");
        } else {
            responseHeader.append("HTTP/1.1 404 Not Found\r\n");
        }
        if (path.equals("/favicon.ico")) {
            responseHeader.append("Content-Type: image/x-icon\r\n\r\n");
        } else {
            appendWhenHTML(responseHeader);
        }
        return responseHeader;
    }

    public void appendWhenHTML(StringBuilder responseHeader) {
        if (!session.containsCookie(clientCookie)) {
            var expirationTime = session.createExpirationTime();
            var cookieName = UUID.randomUUID().toString();
            session.putCookie(cookieName, expirationTime);
            responseHeader.append("Set-Cookie: JSESSIONID=").append(cookieName)
                .append("\r\n");
        } else if (session.cookieHasExpired(clientCookie)) {
            responseHeader.append("Set-Cookie: JSESSIONID=").append(clientCookie)
                .append("; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT")
                .append("\r\n");
        }
        responseHeader.append("Content-Type: text/html;").append("charset=UTF-8\r\n\r\n");
    }
}
