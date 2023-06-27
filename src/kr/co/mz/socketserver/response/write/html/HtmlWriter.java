package kr.co.mz.socketserver.response.write.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import kr.co.mz.socketserver.cache.Cache;
import kr.co.mz.socketserver.request.handle.ClientHandler;
public class HtmlWriter {
    private final StringBuilder httpHeaderBuilder;
    public HtmlWriter(StringBuilder httpResponseBuilder) {
        this.httpHeaderBuilder = httpResponseBuilder;
    }
    public void writeHTML(OutputStream socketOutputStream,String fileName,Cache cache) throws IOException {
        socketOutputStream.write(httpHeaderBuilder.toString().getBytes());
        if(cache.containsHtmlKey(fileName)) {
            int offset = 0;
            byte[] cacheData = cache.getHtmlCache(fileName);
            while (offset < cacheData.length) {
                int length = Math.min(4096, cacheData.length - offset);
                socketOutputStream.write(cacheData, offset, length);
                offset += length;
            }
        }else{
            var file = new File(ClientHandler.PROJECT_DIRECTORY +"/resources/templates" + fileName + ".html");
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
                cache.putHtmlCache(fileName, httpBodyBuilder.toString().getBytes());
            }
            try {
                fileInputStream.close();
            }catch (IOException e){
                System.err.println("An error occurred while closing the buffered file input stream : "+e.getMessage());
            }
        }
    }
}
