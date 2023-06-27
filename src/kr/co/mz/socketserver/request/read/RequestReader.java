package kr.co.mz.socketserver.request.read;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RequestReader {
    public String readRequestLineWithCookie(InputStream inputStream){
        var data = "";
        var resource = new StringBuilder();
        var reader = new BufferedReader( new InputStreamReader(inputStream));
        while(true) {
            try {
                if ((data = reader.readLine()) == null || data.equals(""))
                    break;
            } catch (IOException e) {
                System.err.print("An error occurred while reading the request data : " +e.getMessage());
                throw new RuntimeException(e);
            }
            if(data.startsWith("GET") || data.startsWith("Cookie:")) {
                resource.append(data);
            }
        }
        return resource.toString();
    }
}
