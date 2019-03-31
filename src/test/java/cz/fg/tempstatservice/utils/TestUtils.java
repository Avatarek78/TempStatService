package cz.fg.tempstatservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.fg.tempstatservice.common.TempRange;
import cz.fg.tempstatservice.entities.Temperature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Helper utilities for testing.
 */
public final class TestUtils {

    private static final Logger TestUtils = LoggerFactory.getLogger(TestUtils.class);

    /**
     * To prevent instantiation.
     */
    private TestUtils() {
    }

    /**
     * Method to create test collection of {@link Temperature} entities.
     * @param firstDate {@link Date} of the first record
     * @param recordsCount Required number of records to generate.
     * @param timeStep Time step for Date of next generated record.
     * @param timeUnit {@link TimeUnit} for Date of next generated record.
     * @param lowTemp Lowest {@link Float} value of tempValue for generated Temperatures.
     * @param highTemp Highest {@link Float} value of tempValue for generated Temperatures.
     * @return Generated collection of {@link Temperature} entities.
     */
    public static LinkedList<Temperature> createTestTemperatureCollection(Date firstDate,
                                                                          int recordsCount,
                                                                          int timeStep,
                                                                          TimeUnit timeUnit,
                                                                          Float lowTemp,
                                                                          Float highTemp) {
        LinkedList<Temperature> temperatures = new LinkedList<>();
        Date dateAndTime = firstDate;
        for (long id = 0 ; id < recordsCount ; id++) {
            Temperature temperature = new Temperature();
            temperature.setDateAndTime(dateAndTime);
            dateAndTime = new Date(dateAndTime.getTime() + TimeUnit.MILLISECONDS.convert(timeStep, timeUnit));
            float generatedTemp = lowTemp + new Random().nextFloat() * (highTemp - lowTemp);
            temperature.setTempValue(generatedTemp);
            temperatures.add(temperature);
        }
        return temperatures;
    }

    /**
     * Serialization of object to Json string.
     * @param value Object for serialization.
     * @return Json representation of object as {@link String}
     */
    public static String objToJsonString(Object value) {
        String result = null;
        try {
            result = new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            TestUtils.error("Json serialization failed" ,e);
        }
        return result;
    }

    /**
     * Get min and max temperature value in given list of temperatures.
     * @param temperatures List of {@link Temperature}
     * @return Result as {@link TempRange}
     */
    public static TempRange getMinAndMaxTemperature(LinkedList<Temperature> temperatures) {
        Temperature min = Collections.min(temperatures, Comparator.comparing(Temperature::getTempValue));
        Temperature max = Collections.max(temperatures, Comparator.comparing(Temperature::getTempValue));
        return new TempRange(min.getTempValue(), max.getTempValue());
    }

}
