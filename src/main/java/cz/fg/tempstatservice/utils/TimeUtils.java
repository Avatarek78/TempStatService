package cz.fg.tempstatservice.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Helper utilities for time operations.
 */
public final class TimeUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * LocalDateTime format "yyyy-MM-dd HH:mm:ss.SSS"
     */
    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private TimeUtils() {
    }

    /**
     * Get a diff between two dates
     * @param oldestDate {@link LocalDateTime}
     * @param newestDate {@link LocalDateTime}
     * @return the diff value, in the provided unit.
     */
    public static long getDateDiff(LocalDateTime oldestDate, LocalDateTime newestDate) {
        return ChronoUnit.MILLIS.between(oldestDate, newestDate);
    }

    /**
     * Parse date from string in format {@link TimeUtils#DATE_FORMAT}
     * @param strDate String with date in format {@link TimeUtils#DATE_FORMAT}
     * @return LocalDateTime or null if parsing not succeed.
     */
    public static LocalDateTime dateFromString(String strDate) {
        return LocalDateTime.parse(strDate, TimeUtils.dateFormat);
    }

    public static String dateToString(LocalDateTime date) {
        return TimeUtils.dateFormat.format(date);
    }
}
