package pe.interseguro.dbtest.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomHelper {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime string2dateTime (String datetime) {
        return LocalDateTime.parse(datetime, formatter);
    }

    public static String dateTime2string (LocalDateTime datetime) {
        return datetime.format(formatter);
    }

}
