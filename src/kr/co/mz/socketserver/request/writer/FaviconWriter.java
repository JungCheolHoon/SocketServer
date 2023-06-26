package kr.co.mz.socketserver.request.writer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import kr.co.mz.socketserver.cache.Cache;
import kr.co.mz.socketserver.request.handler.RequestHandler;
public class FaviconWriter {
    public void writeFavicon(BufferedOutputStream bufferedSocketOutputStream, Cache cache) throws IOException {
        var file = new File(RequestHandler.PROJECT_DIRECTORY +"/resources/favicon.ico");
        if(file.exists()){
            bufferedSocketOutputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
        }else{
            bufferedSocketOutputStream.write("HTTP/1.1 404 Not Found\r\n".getBytes());
        }
        bufferedSocketOutputStream.write("Content-Type: image/x-icon\r\n\r\n".getBytes());
        if(cache.containsFaviconKey("/favicon.ico")) {
            int offset = 0;
            byte [] faviconCachedData = cache.getFaviconCache("/favicon.ico");
            while(offset < faviconCachedData.length){
                int length = Math.min(4096, faviconCachedData.length-offset);
                bufferedSocketOutputStream.write(faviconCachedData,offset,length);
                offset += length;
            }
        }
        else{
            try (var fileInputStream = new FileInputStream(file) ; ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream()) {
                // 파일 내용을 읽어서 응답에 추가
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    bufferedSocketOutputStream.write(buffer, 0, bytesRead);
                    arrayOutputStream.write(buffer, 0, bytesRead);
                }
                cache.putFaviconCache("/favicon.ico", arrayOutputStream.toByteArray());
            }
        }
    }
}
