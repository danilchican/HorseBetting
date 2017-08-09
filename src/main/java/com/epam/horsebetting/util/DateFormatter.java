package com.epam.horsebetting.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateFormatter {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Format date.
     *
     * @param format
     * @param date
     * @return date
     */
    public static String format(String format, Timestamp date) {
        return new SimpleDateFormat(format).format(date);
    }
}
