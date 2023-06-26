package kr.co.mz.socketserver.request.parser;
public class RequestParser {
    public String parseRequestToPath(String urlResource){
        String[] parseResources = urlResource.split(" ");
        return parseResources[1].split("\\?")[0];
    }
    public String parseRequestToCookie(String urlResource){
        String[] parseResources = urlResource.split("=");
        return parseResources[parseResources.length-1];
    }
    public String parseRequestToQuery(String urlResource){
        String[] parseResources = urlResource.split(" ");
        String [] parsePath = urlResource.split("\\?");
        return parseResources[1].split("#")[0];
    }
    public String parseRequestToFragment(String urlResource){
        String[] parseResources = urlResource.split(" ");
        return parseResources[1].split("#")[1];
    }
}
