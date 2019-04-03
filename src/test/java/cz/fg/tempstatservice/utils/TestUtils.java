package cz.fg.tempstatservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.fg.tempstatservice.common.TempRange;
import cz.fg.tempstatservice.entities.Temperature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Helper utilities for testing.
 */
public final class TestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Necessary initialization for possibility to customize formatting of LocalDateTime by @JsonFormat. It's very ugly older Date don't need it.
     */
    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * To prevent instantiation.
     */
    private TestUtils() {
    }

    /**
     * Method to create test collection of {@link Temperature} entities.
     * @param firstDate {@link LocalDateTime} of the first record
     * @param recordsCount Required number of records to generate.
     * @param timeStep Time step for LocalDateTime of next generated record.
     * @param timeUnit {@link TimeUnit} for LocalDateTime of next generated record.
     * @param lowTemp Lowest {@link Float} value of tempValue for generated Temperatures.
     * @param highTemp Highest {@link Float} value of tempValue for generated Temperatures.
     * @return Generated collection of {@link Temperature} entities.
     */
    public static LinkedList<Temperature> createTestTemperatureCollection(LocalDateTime firstDate,
                                                                          int recordsCount,
                                                                          int timeStep,
                                                                          TemporalUnit timeUnit,
                                                                          Float lowTemp,
                                                                          Float highTemp) {
        LinkedList<Temperature> temperatures = new LinkedList<>();
        LocalDateTime dateAndTime = firstDate;
        for (long id = 0 ; id < recordsCount ; id++) {
            Temperature temperature = new Temperature();
            temperature.setDateAndTime(dateAndTime);
            dateAndTime = dateAndTime.plus(timeStep, timeUnit);
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
            result = OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            LOGGER.error("Json serialization failed" ,e);
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
