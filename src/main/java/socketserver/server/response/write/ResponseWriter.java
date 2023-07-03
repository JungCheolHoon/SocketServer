package socketserver.server.response.write;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import socketserver.server.DBExecution;
import socketserver.server.handle.ClientHandler;
import socketserver.server.response.dao.CacheDao;
import socketserver.server.response.dto.CacheDto;

public class ResponseWriter {

    private final StringBuilder responseHeader;
    private final OutputStream socketOutputStream;
    private final String path;

    public ResponseWriter(StringBuilder httpResponseBuilder, OutputStream socketOutputStream,
        String uri) {
        this.responseHeader = httpResponseBuilder;
        this.socketOutputStream = socketOutputStream;
        this.path = uri;
    }

    public void write(DBExecution dbExecution) throws IOException {
        socketOutputStream.write(responseHeader.toString().getBytes());
        var file =
            path.equals("/favicon.ico") ?
                new File(ClientHandler.PROJECT_DIRECTORY + "/resources/static" + path)
                : new File(ClientHandler.PROJECT_DIRECTORY + "/resources/templates" + path + ".html");
        Optional<CacheDto> cacheDtoOptional = new CacheDao(dbExecution).select(path);
        if (cacheDtoOptional.isPresent()) {
            CacheDto cacheDto = cacheDtoOptional.get();
            writeCache(cacheDto);
        } else {
            var fileInputStream =
                file.exists() && !file.isDirectory() ?
                    new FileInputStream(file)
                    : new FileInputStream(
                        ClientHandler.PROJECT_DIRECTORY + "/resources/templates/exception/404.html");
            if (file.exists() || !path.equals("/favicon")) {
                writeFile(dbExecution, fileInputStream);
            }
            fileInputStream.close();
        }
    }

    private void writeCache(CacheDto cacheDto) throws IOException {
        byte[] cacheData = cacheDto.getCacheData();
        int offset = 0;
        int chunkSize = 4096;
        while (offset < cacheData.length) {
            int length = Math.min(chunkSize, cacheData.length - offset);
            socketOutputStream.write(cacheData, offset, length);
            offset += length;
        }
        System.out.println("Successful Write DB: " + cacheDto.getPath());
    }

    private void writeFile(DBExecution dbExecution, InputStream fileInputStream) throws IOException {
        try (var arrayOutputStream = new ByteArrayOutputStream()) {
            final byte[] buffer = new byte[4096];
            int readCount;
            while ((readCount = fileInputStream.read(buffer)) != -1) {
                socketOutputStream.write(buffer, 0, readCount);
                arrayOutputStream.write(buffer, 0, readCount);
            }
            String result = new CacheDao(dbExecution).insert(new CacheDto(path, arrayOutputStream.toByteArray()));
            System.out.println("Successful Write File: " + result);
        }
    }
}
