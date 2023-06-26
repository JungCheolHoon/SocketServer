package kr.co.mz.socketserver.request.handler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import kr.co.mz.socketserver.cache.Cache;
import kr.co.mz.socketserver.cookie.Cookie;
import kr.co.mz.socketserver.request.parser.RequestParser;
import kr.co.mz.socketserver.request.reader.RequestReader;
import kr.co.mz.socketserver.request.writer.FaviconWriter;
import kr.co.mz.socketserver.request.writer.HtmlWriter;

public class RequestHandler {
    public static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    public void handleRequest(Socket clientSocket, Cache cache, Cookie cookie) throws IOException {
        try(var reader = new BufferedReader( new InputStreamReader(clientSocket.getInputStream())) ; var bufferedSocketOutputStream = new BufferedOutputStream(clientSocket.getOutputStream()) ){
            var resource = new RequestReader().readRequest(reader);
            var requestParse = new RequestParser();
            if(resource.isEmpty()){
                return;
            }
            String pathResource = requestParse.parseRequestToPath(resource);
            String cookieResource = requestParse.parseRequestToCookie(resource);
            if(pathResource.equals("/favicon.ico")) {
                new FaviconWriter().writeFavicon(bufferedSocketOutputStream,cache);
            }
            // html 요청인 경우
            else{
                var fileName = pathResource.equals("/") ? "/main" : pathResource;
                new HtmlWriter().writeHTML(bufferedSocketOutputStream, fileName, cache, cookie, cookieResource);
            }
        }catch (IOException e){
            System.err.print("An error occurred while parsing : " + e.getMessage());
        }
        try {
            clientSocket.close();
        }catch(IOException e){
            System.err.println("An error occurred when closing the client socket : " + e.getMessage());
        }
    }
}
