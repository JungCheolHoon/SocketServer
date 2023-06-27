package kr.co.mz.socketserver.response.generate;

import java.io.File;
import java.util.UUID;
import kr.co.mz.socketserver.session.Session;
import kr.co.mz.socketserver.request.handle.ClientHandler;
public class HeaderGenerator {
    private final File file;
    public HeaderGenerator(String fileName) {
        this.file = new File(ClientHandler.PROJECT_DIRECTORY +"/resources/templates" + fileName + ".html");
    }
    public StringBuilder generateResponseForFavicon(){
        StringBuilder responseHeader = new StringBuilder();
        if(file.exists() && !file.isDirectory()){
            responseHeader.append("HTTP/1.1 200 OK\r\n");
        }else{
            responseHeader.append("HTTP/1.1 404 Not Found\r\n");
        }
        responseHeader.append("Content-Type: image/x-icon\r\n\r\n");
        return responseHeader;
    }
    public StringBuilder generateResponseForHtml(Session session,String clientCookie){
        var httpResponseBuilder = new StringBuilder();
        if (file.exists() && !file.isDirectory()) {
            httpResponseBuilder.append("HTTP/1.1 200 OK\r\n");
        } else {
            httpResponseBuilder.append("HTTP/1.1 404 Not Found\r\n");
        }
        if(clientCookie.isEmpty()){ // 쿠키가 없다면
            String expirationTime = session.createExpirationTime();
            String cookieName = UUID.randomUUID().toString();
            session.put(cookieName, expirationTime);
            httpResponseBuilder.append("Set-Cookie: SESSIONID=").append(cookieName)
                .append("\r\n");
        } else if(session.cookieHasExpired(clientCookie)) {
            httpResponseBuilder.append("Set-Cookie: SESSIONID=").append(clientCookie).append("; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT")
                .append("\r\n");
        }
        httpResponseBuilder.append("Content-Type: text/html;").append("charset=UTF-8\r\n\r\n");
        return httpResponseBuilder;
    }
}
