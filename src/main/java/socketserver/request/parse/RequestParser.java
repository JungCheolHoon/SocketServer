package socketserver.request.parse;
public class RequestParser {
    private final String httpRequestLineWithCookie;
    private final String uri;
    public RequestParser(String httpRequestLine) {
        this.httpRequestLineWithCookie = httpRequestLine;
        this.uri = httpRequestLine.split(" ")[1];
    }
    public String getURI(){
        return uri;
    }
    public String getCookie(){
        if(httpRequestLineWithCookie.contains("SESSIONID")) {
            String[] splitHttpRequest = httpRequestLineWithCookie.split("=");
            return splitHttpRequest[splitHttpRequest.length - 1];
        }else{
            return "";
        }
    }
    public String getQuery(){
        String [] splitUri = uri.split("\\?");
        if(splitUri.length>2) {
            return splitUri[1].split("#")[0];
        }else{
            return "";
        }
    }
    public String getFragment(){
        String [] splitUri = uri.split("#");
        if(splitUri.length>2) {
            return splitUri[1];
        }else{
            return "";
        }
    }
}
