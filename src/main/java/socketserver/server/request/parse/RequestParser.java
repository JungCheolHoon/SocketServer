package socketserver.server.request.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class RequestParser {

    private final BufferedReader reader;
    private URL url;

    public RequestParser(InputStream inputStream) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        String requestLine = reader.readLine();
        if (requestLine != null) {
            String[] requestLineArr = requestLine.split(" ");
            this.url = requestLineArr.length == 3 ? new URL("http://" + requestLineArr[1]) : null;
        }
    }

    public String getPath() {
        if (url == null) {
            return "";
        } else {
            return url.getPath();
        }
    }

    public String parseToSessionId() throws IOException {
        String data;
        var jSessionId = "";
        while ((data = reader.readLine()) != null && !data.equals("")) {
            if (data.startsWith("Cookie:") && data.contains("JSESSIONID")) {
                String[] jSessionInfo = data.split("=");
                jSessionId = jSessionInfo[jSessionInfo.length - 1];
            }
        }
        return jSessionId;
    }

}
