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
    public String get(String key){
        return clientCookie.get(key);
    }
    public Boolean contains(String key){
        return clientCookie.containsKey(key);
    }
    public void put(String key,String value){
        clientCookie.put(key,value);
    }
    public Boolean cookieHasExpired(String clientCookie){
        var dateTimeConverter = new DateTimeConverter();
        ZonedDateTime cookieExpireTime = dateTimeConverter.convertStringToDate(get(clientCookie));
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"));
        if(contains(clientCookie) && cookieExpireTime.isBefore(now)) {
            remove(clientCookie);
            return true;
        }
        return false;
    }
    public void remove(String key){
        clientCookie.remove(key);
    }
    public String createExpirationTime(){
        ZoneId gmtZone = ZoneId.of("GMT");
        ZonedDateTime expirationTime = ZonedDateTime.now(gmtZone).plusSeconds(20);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss z",
            Locale.ENGLISH);
        return expirationTime.format(formatter);
    }
}
