package kr.co.mz.socketserver.cookie;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import kr.co.mz.socketserver.conversion.DateTimeConverter;

public class Cookie {
    private final HashMap<String, String> clientCookie;
    public Cookie() {
        clientCookie = new HashMap<>();
    }
    public String getCookie(String key){
        return clientCookie.get(key);
    }
    public void putCookie(String key,String value){
        clientCookie.put(key,value);
    }
    public boolean cookieHasExpired(String clientCookie){
        var dateTimeConverter = new DateTimeConverter();
        Calendar cookieExpireTime = dateTimeConverter.convertStringToDate(getCookie(clientCookie));
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        return cookieExpireTime.before(now);
    }
    public void removeCookie(String key){
        clientCookie.remove(key);
    }
    public String createExpirationTime(){
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.add(Calendar.SECOND,20);
        Date date = calendar.getTime();
        SimpleDateFormat expirationTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return expirationTimeFormat.format(date);
    }
}
