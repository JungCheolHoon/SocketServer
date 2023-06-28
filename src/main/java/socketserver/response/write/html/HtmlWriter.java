package socketserver.response.write.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import socketserver.cache.Cache;
import socketserver.handle.ClientHandler;

public class HtmlWriter {
    private final StringBuilder httpHeaderBuilder;
    private final OutputStream socketOutputStream;
    private final String uri;
    public HtmlWriter(StringBuilder httpResponseBuilder, OutputStream socketOutputStream,
        String uri) {
        this.httpHeaderBuilder = httpResponseBuilder;
        this.socketOutputStream = socketOutputStream;
        this.uri = uri;
    }
    public void writeHTML(Cache cache, Connection connection) throws IOException {
        socketOutputStream.write(httpHeaderBuilder.toString().getBytes());
        if(cache.containsHtmlKey(uri)) {
            int offset = 0;
            byte[] cacheData = cache.getHtmlCache(uri);
            while (offset < cacheData.length) {
                int length = Math.min(4096, cacheData.length - offset);
                socketOutputStream.write(cacheData, offset, length);
                offset += length;
            }
        }else{
            var file = new File(ClientHandler.PROJECT_DIRECTORY +"/resources/templates" + uri + ".html");
            var httpBodyBuilder = new StringBuilder();
            InputStream fileInputStream = new FileInputStream(ClientHandler.PROJECT_DIRECTORY + "/resources/templates/exception/404.html");
            if (file.exists() && !file.isDirectory()) {
                fileInputStream = new FileInputStream(file);
            }
            int bytesRead;
            byte[] buffer = new byte[4096];
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                socketOutputStream.write(buffer, 0, bytesRead);
                httpBodyBuilder.append(new String(buffer,0,bytesRead));
            }
            if(file.exists()){
                cache.putHtmlCache(uri, httpBodyBuilder.toString().getBytes());
            }
            try {
                fileInputStream.close();
            }catch (IOException e){
                System.err.println("An error occurred while closing the buffered file input stream : "+e.getMessage());
            }
        }
    }
}
