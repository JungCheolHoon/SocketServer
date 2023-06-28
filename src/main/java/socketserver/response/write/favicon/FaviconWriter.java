package socketserver.response.write.favicon;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import socketserver.cache.Cache;
import socketserver.handle.ClientHandler;

public class FaviconWriter {
    private final StringBuilder faviconResponseHeader;
    private final OutputStream socketOutputStream;
    private final String uri;
    public FaviconWriter(StringBuilder faviconResponseHeader, OutputStream outputStream, String uri) {
        this.faviconResponseHeader = faviconResponseHeader;
        this.socketOutputStream = outputStream;
        this.uri = uri;
    }
    public void writeFavicon(Cache cache, Connection connection) throws IOException {
        var file = new File(ClientHandler.PROJECT_DIRECTORY +"/resources" + uri);
        socketOutputStream.write(faviconResponseHeader.toString().getBytes());
        if(cache.containsFaviconKey(uri)) {
            int offset = 0;
            byte [] faviconCachedData = cache.getFaviconCache(uri);
            while(offset < faviconCachedData.length){
                int length = Math.min(4096, faviconCachedData.length-offset);
                socketOutputStream.write(faviconCachedData,offset,length);
                offset += length;
            }
        }
        else if(file.exists()){
            try (var fileInputStream = new FileInputStream(file) ; ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream()) {
                int bytesRead;
                byte[] buffer = new byte[4096];
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    socketOutputStream.write(buffer, 0, bytesRead);
                    arrayOutputStream.write(buffer, 0, bytesRead);
                }
                cache.putFaviconCache(uri, arrayOutputStream.toByteArray());
            }
        }
    }
}
