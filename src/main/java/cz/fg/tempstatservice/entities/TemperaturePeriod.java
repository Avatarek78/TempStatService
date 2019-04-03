package cz.fg.tempstatservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.fg.tempstatservice.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Class with time period data. Contains start and end date, min and max temperature values and the number of measurements of this period.
 */
@Getter
@AllArgsConstructor
@ToString
public class TemperaturePeriod implements Comparable<TemperaturePeriod> {

    @JsonFormat(pattern = TimeUtils.DATE_FORMAT)
    private LocalDateTime startOfPeriod;

    @JsonFormat(pattern = TimeUtils.DATE_FORMAT)
    private LocalDateTime endOfPeriod;

    private Float minTemp;
    private Float maxTemp;
    private BigInteger countOfMeasurements;

    @Override
    public int compareTo(TemperaturePeriod o) {
        Long thisTimeDiff = TimeUtils.getDateDiff(startOfPeriod, endOfPeriod);
        Long otherTimeDiff = TimeUtils.getDateDiff(o.startOfPeriod, o.endOfPeriod);
        int result = thisTimeDiff.compareTo(otherTimeDiff);
        // If periods are same by time difference then compare them by countOfMeasurements
        if (result == 0) {
            result = countOfMeasurements.compareTo(o.countOfMeasurements);
        }
        // If still no difference then compare them by temperature range. Biggest range is winner.
        if (result == 0) {
            Float thisTempDiff = maxTemp - minTemp;
            Float otherTempDiff = o.maxTemp - o.minTemp;
            result = thisTempDiff.compareTo(otherTempDiff);
        }
        return result;
    }
}
