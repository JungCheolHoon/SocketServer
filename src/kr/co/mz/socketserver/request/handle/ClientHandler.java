package kr.co.mz.socketserver.request.handle;

import java.io.IOException;
import java.net.Socket;
import kr.co.mz.socketserver.cache.Cache;
import kr.co.mz.socketserver.session.Session;
import kr.co.mz.socketserver.request.parse.RequestParser;
import kr.co.mz.socketserver.request.read.RequestReader;
import kr.co.mz.socketserver.response.generate.HeaderGenerator;
import kr.co.mz.socketserver.response.write.favicon.FaviconWriter;
import kr.co.mz.socketserver.response.write.html.HtmlWriter;
public class ClientHandler {
    public static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    private final Socket clientSocket;
    private final Cache cache;
    private final Session session;
    public ClientHandler(Socket clientSocket, Cache cache,Session session) {
        this.clientSocket = clientSocket;
        this.cache = cache;
        this.session = session;
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
                new FaviconWriter(faviconResponseHeader, socketOutputStream, uri).writeFavicon(cache);
            }
            else{
                uri = uri.equals("/") ? "/main" : uri;
                var httpResponseBuilder = new HeaderGenerator(uri).generateResponseForHtml(session,clientCookie);
                new HtmlWriter(httpResponseBuilder, socketOutputStream, uri).writeHTML(cache);
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
