package socketserver.handle;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import socketserver.cache.Cache;
import socketserver.session.Session;
import socketserver.request.parse.RequestParser;
import socketserver.request.read.RequestReader;
import socketserver.response.generate.HeaderGenerator;
import socketserver.response.write.favicon.FaviconWriter;
import socketserver.response.write.html.HtmlWriter;
public class ClientHandler {
    public static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    private final Socket clientSocket;
    private final Cache cache;
    private final Session session;
    private final Connection connection;
    public ClientHandler(Socket clientSocket, Cache cache,Session session, Connection connection) {
        this.clientSocket = clientSocket;
        this.cache = cache;
        this.session = session;
        this.connection = connection;
    }
    public void handle() throws IOException {
        try(var inputStream = clientSocket.getInputStream() ; var socketOutputStream = clientSocket.getOutputStream() ){
            var requestLineWithCookie = new RequestReader(inputStream).readRequestLineWithCookie();
            if(requestLineWithCookie.isEmpty()){
                return;
            }
            var requestParse = new RequestParser(requestLineWithCookie);
            var uri = requestParse.getURI();
            var clientCookie = requestParse.getCookie();
            if(uri.equals("/favicon.ico")) {
                var faviconResponseHeader = new HeaderGenerator(uri).generateResponseForFavicon();
                new FaviconWriter(faviconResponseHeader, socketOutputStream, uri).writeFavicon(cache, connection);
            }
            else{
                uri = uri.equals("/") ? "/main" : uri;
                var httpResponseBuilder = new HeaderGenerator(uri).generateResponseForHtml(session,clientCookie);
                new HtmlWriter(httpResponseBuilder, socketOutputStream, uri).writeHTML(cache,connection);
            }
        }catch (IOException e){
            System.err.print("An error occurred while parsing : " + e.getMessage());
        }
    }
    public void close(){
        try {
            clientSocket.close();
        }catch(IOException e){
            System.err.println("An error occurred when closing the client socket : " + e.getMessage());
        }
    }
}
