package kr.co.mz.socketserver.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import kr.co.mz.socketserver.conversion.DateTimeConverter;

public class RequestHandler {
    private static final String projectDirectory = System.getProperty("user.dir");
    private static final Map<String, byte[]> faviconCache = new HashMap<>();
    private static final Map<String, String> htmlCache = new HashMap<>();
    private static final Map<String, String> session = new HashMap<>();

    public void handleRequest(Socket clientSocket) throws IOException {

        var reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        var requestHandler = new RequestHandler();

        var resource = "";
        var urlResource = new StringBuilder();

        System.out.println("==========================================");
        while(!(resource = reader.readLine()).equals("")) {
            urlResource.append(resource);
            //System.out.println(resource);
        }
        String[] cookie = urlResource.toString().split("=");
        //System.out.println("쿠키에 대한 데이터 : "+cookie[cookie.length-1]);

        String[] files = urlResource.toString().split(" ");
        //System.out.println("urlResource : " + files[1]);
        // favicon.ico 요청인 경우
        if(files[1].equals("/favicon.ico")) {
            requestHandler.writeFavicon(clientSocket);
        }
        // html 요청인 경우
        else{
            var fileName = files[1].equals("/") ? "/main" : files[1];
            requestHandler.writeHTML(clientSocket, fileName, cookie[cookie.length-1]);
        }

        reader.close();
        clientSocket.close();
    }// RequestHandler

    private void writeHTML(Socket clientSocket,String fileName,String clientCookie) throws IOException {

        var writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        var httpResponseBuilder = new StringBuilder();

        var dateTimeConverter = new DateTimeConverter();

        // 캐시가 있을때 응답 생성
        if(RequestHandler.htmlCache.containsKey(fileName)) {
            Calendar cookieExpireTime = dateTimeConverter.convertStringToDate(RequestHandler.session.get(clientCookie));
            Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
            System.out.println("현재 시간 : " + now.getTime());
            System.out.println("만료 시간 : " + RequestHandler.session.get(clientCookie));
            if(cookieExpireTime.before(now)){
                RequestHandler.htmlCache.remove(fileName);
                RequestHandler.session.remove(clientCookie);
                new RequestHandler().writeHTML(clientSocket,fileName,clientCookie);
                System.out.println("캐싱 및 쿠키 삭제되었음");
            }else{
                writer.write(RequestHandler.htmlCache.get(fileName));
                System.out.println("캐싱 및 쿠키 삭제 안되었음");
            }
        }else{
            // 캐시가 없을때 응답 생성
            var file = new File(projectDirectory+"/resources/templates" + fileName + ".html");
            BufferedReader fileReader;

            if (file.isDirectory() || !file.exists()) {
                httpResponseBuilder.append("HTTP/1.1 404 Not Found\r\n");
                fileReader = new BufferedReader(new InputStreamReader((new FileInputStream(
                    projectDirectory+"/resources/templates/exception/404.html"))));
            } else {
                httpResponseBuilder.append("HTTP/1.1 200 OK\r\n");
                fileReader = new BufferedReader(
                    new InputStreamReader((new FileInputStream(file))));
            }

            TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
            Calendar calendar = Calendar.getInstance(timeZone);
            // 쿠키 만료시간을 10초로 설정
            calendar.add(Calendar.SECOND,20);

            Date date = calendar.getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(date);
            System.out.println("쿠키 만료시간 : " + formattedDate);

            // 세션에 쿠키이름에 해당하는 만료시간 저장
            String cookie = UUID.randomUUID().toString();
            RequestHandler.session.put(cookie,formattedDate);

            // Set-Cookie 헤더 설정
            httpResponseBuilder.append("Set-Cookie: SESSIONID=").append(cookie).append("; Expires=").append(formattedDate).append("\r\n");
            httpResponseBuilder.append("Content-Type: text/html;").append("charset=UTF-8\r\n\r\n");


            var requestLine = "";
            while ((requestLine = fileReader.readLine()) != null) {
                httpResponseBuilder.append(requestLine).append("\r\n");
            }

            if(file.exists()){
                RequestHandler.htmlCache.put(fileName,httpResponseBuilder.toString());
            }
            writer.write(httpResponseBuilder.toString());
            fileReader.close();
        }
        // 연결 종료
        writer.flush();
        writer.close();
    }// writeHTML

    private void writeFavicon(Socket clientSocket) throws IOException {

        var writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        var file = new File(projectDirectory+"/resources/favicon.ico");

        if(file.exists()){
            writer.write("HTTP/1.1 200 OK\r\n");
        }else{
            writer.write("HTTP/1.1 404 Not Found\r\n");
        }
        writer.write("Content-Type: image/x-icon\r\n\r\n");
        writer.flush();

        // 캐싱된 favicon 데이터인 경우
        if(RequestHandler.faviconCache.containsKey("/favicon.ico")) {
            var outputStream = clientSocket.getOutputStream();
            int offset = 0;
            byte [] faviconCachedData = RequestHandler.faviconCache.get("/favicon.ico");
            while(offset < faviconCachedData.length){
                int length = Math.min(4096, faviconCachedData.length-offset);
                outputStream.write(faviconCachedData,offset,length);
                offset += length;
            }
            outputStream.close();
        }
        // 캐싱되어있지 않은 경우
        else{
            try (var fileInputStream = new FileInputStream(file)) {
                ByteArrayOutputStream arrayOutputStream;
                var outputStream = clientSocket.getOutputStream();
                arrayOutputStream = new ByteArrayOutputStream();
                // 파일 내용을 읽어서 응답에 추가
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    arrayOutputStream.write(buffer, 0, bytesRead);
                }
                RequestHandler.faviconCache.put("/favicon.ico", arrayOutputStream.toByteArray());
                arrayOutputStream.close();
                outputStream.close();
            }
        }
        writer.close();
    }// writeFavicon
}// class
