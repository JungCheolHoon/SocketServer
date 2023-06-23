package kr.co.mz.socketserver.conversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DateTimeConverter {
    public Calendar convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        // Date 객체를 Calendar 객체로 변환
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(dateString));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return calendar;
    }// convertStringToDate
}// class
