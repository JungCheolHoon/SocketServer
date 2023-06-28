package socketserver.convert;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeConverter {
    private final String expirationTimeStr;
    public DateTimeConverter(String expirationTimeStr) {
        this.expirationTimeStr = expirationTimeStr;
    }
    public ZonedDateTime convertStringToDate() {
        var formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        var dateTime = ZonedDateTime.parse(expirationTimeStr, formatter);
        return dateTime.withZoneSameInstant(ZoneId.of("GMT"));
    }
}
