package socketserver.server.request.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import socketserver.server.model.URN;

public class RequestParser {

    private final BufferedReader reader;
    private final URN urn;

    public RequestParser(InputStream inputStream) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.urn = new URN();
    }

    public URN parseToURN() throws IOException {
        var requestLine = reader.readLine();
        if (requestLine != null && !requestLine.isEmpty()) {
            var urnStr = requestLine.split(" ")[1];
            String[] urnArr = urnStr.split("\\?");
            var path = urnArr[0];
            urn.setPath(path);
            if (urnArr.length > 2) {
                parseToQueryAndFragment(urnArr[1]);
            }
        }
        return urn;
    }

    public void parseToQueryAndFragment(String urnInfo) {
        String[] urnArrWithoutPath = urnInfo.split("#");
        var query = urnArrWithoutPath[0];
        urn.setQuery(query);
        if (urnArrWithoutPath.length > 2) {
            String fragment = urnArrWithoutPath[1];
            urn.setFragment(fragment);
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
