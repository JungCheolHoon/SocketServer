package kr.co.mz.socketserver.convert;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeConverter {
    public ZonedDateTime convertStringToDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        ZonedDateTime dateTime = ZonedDateTime.parse(dateString, formatter);
        return dateTime.withZoneSameInstant(ZoneId.of("GMT"));
    }
}
