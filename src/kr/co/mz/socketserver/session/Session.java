package kr.co.mz.socketserver.session;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import kr.co.mz.socketserver.convert.DateTimeConverter;

public class Session {
    private final HashMap<String, String> clientCookie;
    public Session() {
        clientCookie = new HashMap<>();
    }
    public String getCookie(String key){
        return clientCookie.get(key);
    }
    public Boolean containsCookie(String key){
        return clientCookie.containsKey(key);
    }
    public void putCookie(String key,String value){
        clientCookie.put(key,value);
    }
    public Boolean cookieHasExpired(String clientCookie){
        var cookieExpireTime = new DateTimeConverter(getCookie(clientCookie)).convertStringToDate();
        var now = ZonedDateTime.now(ZoneId.of("GMT"));
        if(containsCookie(clientCookie) && cookieExpireTime.isBefore(now)) {
            remove(clientCookie);
            return true;
        }
        return false;
    }
    public void remove(String key){
        clientCookie.remove(key);
    }
    public String createExpirationTime(){
        var gmtZone = ZoneId.of("GMT");
        var expirationTime = ZonedDateTime.now(gmtZone).plusSeconds(20);
        var formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss z",
            Locale.ENGLISH);
        return expirationTime.format(formatter);
    }
}
