package com.epam.horsebetting.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateFormatter {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final int HOUR_TIME = 36_000_00;

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

    /**
     * Calculate difference between two dates.
     *
     * @param fDate
     * @param sDate
     * @param timeUnit
     * @return difference
     */
    public static long calcDateDiff(Timestamp fDate, Timestamp sDate, TimeUnit timeUnit) {
        long diff = sDate.getTime() - fDate.getTime();
        return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
    }
}
