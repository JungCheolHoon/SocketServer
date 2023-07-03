package socketserver.server.session;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import socketserver.server.session.convert.SessionTimeConverter;

public class Session {

    private final HashMap<String, String> clientSessionId;

    public Session() {
        clientSessionId = new HashMap<>();
    }

    public String getSessionId(String key) {
        return clientSessionId.get(key);
    }

    public void putSession(String key, String value) {
        clientSessionId.put(key, value);
    }

    public Boolean containsSessionId(String key) {
        return clientSessionId.containsKey(key);
    }

    public Boolean sessionHasExpired(String clientCookie) {
        var cookieExpireTime = new SessionTimeConverter(getSessionId(clientCookie)).convertStringToDate();
        var now = ZonedDateTime.now(ZoneId.of("GMT"));
        if (containsSessionId(clientCookie) && cookieExpireTime.isBefore(now)) {
            remove(clientCookie);
            return true;
        }
        return false;
    }

    public String createSessionExpirationTime() {
        var gmtZone = ZoneId.of("GMT");
        var expirationTime = ZonedDateTime.now(gmtZone).plusSeconds(20);
        var formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss z",
            Locale.ENGLISH);
        return expirationTime.format(formatter);
    }

    public void remove(String key) {
        clientSessionId.remove(key);
    }
}
