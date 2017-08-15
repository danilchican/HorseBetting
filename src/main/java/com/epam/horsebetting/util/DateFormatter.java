package com.epam.horsebetting.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormatter {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Format date by locale.
     *
     * @param date
     * @param locale
     * @return formatted date
     */
    public static String format(Timestamp date, Locale locale) {
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale).format(date);
    }
}
