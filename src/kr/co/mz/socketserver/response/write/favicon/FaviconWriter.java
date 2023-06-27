package kr.co.mz.socketserver.response.write.favicon;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import kr.co.mz.socketserver.cache.Cache;
import kr.co.mz.socketserver.request.handle.ClientHandler;
public class FaviconWriter {
    private final StringBuilder faviconResponseHeader;
    public FaviconWriter(StringBuilder faviconResponseHeader) {
        this.faviconResponseHeader = faviconResponseHeader;
    }
    public void writeFavicon(OutputStream outputStream, String fileName, Cache cache) throws IOException {
        var file = new File(ClientHandler.PROJECT_DIRECTORY +"/resources" + fileName);
        outputStream.write(faviconResponseHeader.toString().getBytes());
        if(cache.containsFaviconKey(fileName)) {
            int offset = 0;
            byte [] faviconCachedData = cache.getFaviconCache(fileName);
            while(offset < faviconCachedData.length){
                int length = Math.min(4096, faviconCachedData.length-offset);
                outputStream.write(faviconCachedData,offset,length);
                offset += length;
            }
        }
        else if(file.exists()){
            try (var fileInputStream = new FileInputStream(file) ; ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream()) {
                int bytesRead;
                byte[] buffer = new byte[4096];
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    arrayOutputStream.write(buffer, 0, bytesRead);
                }
                cache.putFaviconCache(fileName, arrayOutputStream.toByteArray());
            }
        }
    }
}
