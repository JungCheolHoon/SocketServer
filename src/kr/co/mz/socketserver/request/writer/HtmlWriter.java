package kr.co.mz.socketserver.request.writer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import kr.co.mz.socketserver.cache.Cache;
import kr.co.mz.socketserver.cookie.Cookie;
import kr.co.mz.socketserver.request.handler.RequestHandler;
public class HtmlWriter {
    public void writeHTML(BufferedOutputStream bufferedSocketOutputStream,String fileName,Cache cache, Cookie cookie, String cookieResource) throws IOException {
        var httpResponseBuilder = new StringBuilder();
        if(cache.containsHtmlKey(fileName)) {
            if(cookie.cookieHasExpired(cookieResource)){
                cache.removeHtmlCache(fileName);
                cookie.removeCookie(cookieResource);
                writeHTML(bufferedSocketOutputStream,fileName,cache,cookie,cookieResource);
            }else{
                System.out.println("캐싱된 데이터입니다.");
                byte[] cacheData = cache.getHtmlCache(fileName);
                int offset = 0;
                int chunkSize = 4096;
                while (offset < cacheData.length) {
                    int length = Math.min(chunkSize, cacheData.length - offset);
                    bufferedSocketOutputStream.write(cacheData, offset, length);
                    offset += length;
                }
            }
        }else{
            var file = new File(RequestHandler.PROJECT_DIRECTORY +"/resources/templates" + fileName + ".html");
            BufferedInputStream bufferedFileInputStream;

            if (file.isDirectory() || !file.exists()) {
                httpResponseBuilder.append("HTTP/1.1 404 Not Found\r\n");
                bufferedFileInputStream = new BufferedInputStream((new FileInputStream(
                    RequestHandler.PROJECT_DIRECTORY +"/resources/templates/exception/404.html")));
            } else {
                httpResponseBuilder.append("HTTP/1.1 200 OK\r\n");
                bufferedFileInputStream = new BufferedInputStream(new FileInputStream(file));
            }
            String expirationTime = cookie.createExpirationTime();
            String cookieName = UUID.randomUUID().toString();
            cookie.putCookie(cookieName,expirationTime);
            httpResponseBuilder.append("Set-Cookie: SESSIONID=").append(cookieName).append("; Expires=").append(expirationTime).append("\r\n");
            httpResponseBuilder.append("Content-Type: text/html;").append("charset=UTF-8\r\n\r\n");
            bufferedSocketOutputStream.write(httpResponseBuilder.toString().getBytes());
            int bytesRead;
            byte[] buffer = new byte[4096];
            while ((bytesRead = bufferedFileInputStream.read(buffer)) != -1) {
                bufferedSocketOutputStream.write(buffer, 0, bytesRead);
                httpResponseBuilder.append(new String(buffer,0,bytesRead));
            }
            if(file.exists()){
                cache.putHtmlCache(fileName,httpResponseBuilder.toString().getBytes());
            }
            try {
                bufferedFileInputStream.close();
            }catch (IOException e){
                System.err.println("An error occurred while closing the buffered file input stream : "+e.getMessage());
            }
        }
    }// writeHTML

}
