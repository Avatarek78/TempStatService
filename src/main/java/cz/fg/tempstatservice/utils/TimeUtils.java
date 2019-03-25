package cz.fg.tempstatservice.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Helper utilities for time operations.
 */
public final class TimeUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * Date format "yyyy-MM-dd HH:mm:ss.SSS"
     */
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

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
}
