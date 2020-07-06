package ba.unsa.etf.zavrsni.app.utils;

import java.sql.Timestamp;

public class DateUtil {

    public static Timestamp parseTimestamp(Long timestamp) {
        return new Timestamp(timestamp);
    }

    public static Timestamp parseTimestamp(String timestamp) {
        return new Timestamp(Long.parseLong(timestamp));
    }
}
