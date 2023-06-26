package kr.co.mz.socketserver.request.reader;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestReader {
    public String readRequest(BufferedReader reader){
        var data = "";
        var resource = new StringBuilder();

        while(true) {
            try {
                if ((data = reader.readLine()) == null || data.equals(""))
                    break;
            } catch (IOException e) {
                System.err.print("An error occurred while reading the request data : " +e.getMessage());
                throw new RuntimeException(e);
            }
            resource.append(data);
        }
        return resource.toString();
    }
}
