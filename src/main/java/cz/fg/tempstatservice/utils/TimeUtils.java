package cz.fg.tempstatservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Helper utilities for time operations.
 */
public final class TimeUtils {

    private static final Logger logger = LoggerFactory.getLogger(TimeUtils.class);
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * Date format "yyyy-MM-dd HH:mm:ss.SSS"
     */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    private TimeUtils() {
    }

    /**
     * Get a diff between two dates
     * @param oldestDate {@link Date}
     * @param newestDate {@link Date}
     * @param timeUnit {@link TimeUnit} in which you want the diff
     * @return the diff value, in the provided unit.
     */
    public static long getDateDiff(Date oldestDate, Date newestDate, TimeUnit timeUnit) {
        long diffInMilliseconds = newestDate.getTime() - oldestDate.getTime();
        return timeUnit.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
    }

    /**
     * Parse date from string in format {@link TimeUtils#DATE_FORMAT}
     * @param strDate String with date in format {@link TimeUtils#DATE_FORMAT}
     * @return Date or null if parsing not succeed.
     */
    public static Date dateFromString(String strDate) {
        try {
            return TimeUtils.dateFormat.parse(strDate);
        } catch (ParseException e) {
            logger.error("Date parse error", e);
            return null;
        }
    }

    public static String dateToString(Date date) {
        return TimeUtils.dateFormat.format(date);
    }
}
