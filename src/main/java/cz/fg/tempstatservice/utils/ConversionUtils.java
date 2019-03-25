package cz.fg.tempstatservice.utils;

import cz.fg.tempstatservice.entities.TemperaturePeriod;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

public final class ConversionUtils {

    private ConversionUtils() {
    }

    /**
     * Method converts list of raw data with temperature periods into list of {@link TemperaturePeriod}
     * @param periodData Raw data as list of {@link Object} arrays
     * @return list of {@link TemperaturePeriod}
     */
    public static ArrayList<TemperaturePeriod> convertRawDataToTempPeriods(ArrayList<Object[]> periodData) {
        ArrayList<TemperaturePeriod> temperaturePeriods = new ArrayList<>(periodData.size());
        // Converts raw data into comparable TemperaturePeriod instances.
        periodData.forEach(object -> {
            TemperaturePeriod period = new TemperaturePeriod(
                    (Date) object[0],       // startOfPeriod
                    (Date) object[1],       // endOfPeriod
                    objToFloat(object[2]),  // minTemp
                    objToFloat(object[3]),  // maxTemp
                    (BigInteger) object[4]  //countOfMeasurements
            );
            temperaturePeriods.add(period);
        });
        return temperaturePeriods;
    }

    public static Float objToFloat(Object obj) {
        Class cls = obj.getClass();
        if (cls == Float.class) {
            return (Float) obj;
        } else if (cls == Double.class) {
            return ((Double) obj).floatValue();
        } else {
            throw new IllegalArgumentException("Object must be Float or Double");
        }
    }
}
