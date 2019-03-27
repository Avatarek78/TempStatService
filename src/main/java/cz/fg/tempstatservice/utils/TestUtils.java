package cz.fg.tempstatservice.utils;

import cz.fg.tempstatservice.entities.Temperature;

import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Helper utilities for testing.
 */
public final class TestUtils {

    /**
     * To prevent instantiation.
     */
    private TestUtils() {
    }

    /**
     * Method to create test collection of {@link Temperature} entities.
     * @param firstDate {@link Date} of the first record
     * @param firstId ID of the first record.
     * @param recordsCount Required number of records to generate.
     * @param timeStep Time step for Date of next generated record.
     * @param timeUnit {@link TimeUnit} for Date of next generated record.
     * @param lowTemp Lowest {@link Float} value of tempValue for generated Temperatures.
     * @param highTemp Highest {@link Float} value of tempValue for generated Temperatures.
     * @return Generated collection of {@link Temperature} entities.
     */
    public static LinkedList<Temperature> createTestTemperatureCollection(Date firstDate,
                                                                          int firstId,
                                                                          int recordsCount,
                                                                          int timeStep,
                                                                          TimeUnit timeUnit,
                                                                          Float lowTemp,
                                                                          Float highTemp) {
        LinkedList<Temperature> temperatures = new LinkedList<>();
        Date dateAndTime = firstDate;
        for (long id = firstId ; id < recordsCount + firstId ; id++) {
            Temperature temperature = new Temperature();
            temperature.setId(id);
            temperature.setDateAndTime(dateAndTime);
            dateAndTime = new Date(dateAndTime.getTime() + TimeUnit.MILLISECONDS.convert(timeStep, timeUnit));
            float generatedTemp = lowTemp + new Random().nextFloat() * (highTemp - lowTemp);
            temperature.setTempValue(generatedTemp);
            temperatures.add(temperature);
        }
        return temperatures;
    }

}
