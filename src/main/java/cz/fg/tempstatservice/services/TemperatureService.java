package cz.fg.tempstatservice.services;

import cz.fg.tempstatservice.entities.TemperaturePeriod;
import cz.fg.tempstatservice.entities.Temperature;
import cz.fg.tempstatservice.exceptions.IdException;
import cz.fg.tempstatservice.repositories.TemperatureRepository;
import cz.fg.tempstatservice.utils.ConversionUtils;
import cz.fg.tempstatservice.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class for all functionality around Temperature.
 */
@Service
public class TemperatureService {

    private final Logger logger = LoggerFactory.getLogger(TemperatureService.class);

    @Autowired
    private TemperatureRepository temperatureRepository;


    /**
     * @return All Temperature records from DB.
     */
    public Iterable<Temperature> getAllTemperatures() {
        return temperatureRepository.findAll();
    }

    /**
     * Save new or update existing Temperature record in DB.
     * @param temperature Temperature record to save or update.
     * @return Saved or updated Temperature record.
     */
    public Temperature saveOrUpdateTemperature(Temperature temperature) {
        return temperatureRepository.save(temperature);
    }

    /**
     * Returns Temperature record by DB id.
     * @param id Temperature record DB id to find.
     * @return Temperature record if exists otherwise throws IdException.
     * @throws IdException when entity doesn't exists in DB.
     */
    public Temperature findByById(Long id) throws IdException {
        Temperature temperature = temperatureRepository.findById(id).orElse(null);
        if (temperature == null) {
            throw new IdException("Temperature with ID '" + id + "' doesn't exists");
        }
        return temperature;
    }

    /**
     * Delete Temperature record by DB id.
     * @param id DB id to find.
     * @throws IdException when record doesn't exists in DB.
     */
    public void deleteTemperatureById(Long id) throws IdException {
        if (!temperatureRepository.existsById(id)) {
            throw new IdException("Cannot delete Temperature with ID '" + id + "'. This Temperature doesn't exists");
        }
        temperatureRepository.deleteById(id);
    }

    /**
     * @see cz.fg.tempstatservice.repositories.TemperatureRepository#findByTempRange(Float, Float)
     */
    public Iterable<Temperature> findByTempRange(Float lowTemp, Float highTemp) {
        return temperatureRepository.findByTempRange(lowTemp, highTemp);
    }

    /**
     * @see cz.fg.tempstatservice.repositories.TemperatureRepository#findByDateAndTime(Date, Date)
     */
    public Iterable<Temperature> findByDateAndTime(Date dateFrom, Date dateTo) {
        return temperatureRepository.findByDateAndTime(dateFrom, dateTo);
    }

    /**
     * Getting value of start and end date of the longest period when temperatures didn't fall below the value lowTemp
     * and at the same time temperatures didn't rise above the value highTemp.
     * @param lowTemp {@link Float}
     * @param highTemp {@link Float}
     * @return result as {@link TemperaturePeriod} or null if no data found by given criteria.
     */
    public TemperaturePeriod findLongestPeriodByTempRange(Float lowTemp, Float highTemp) {
        ArrayList<Object[]> periodData = (ArrayList<Object[]>) temperatureRepository.findLongestPeriodByTempRange(lowTemp, highTemp);
        return getLongestPeriodFromData(periodData);
    }

    /**
     * Getting value of start and end date of the longest period when temperatures didn't fall below the value lowTemp
     * and at the same time temperatures didn't rise above the value highTemp. But with extra condition when hours
     * of measuring must be between the entered values hourFrom and hourTo for each day.
     * @param lowTemp {@link Float}
     * @param highTemp {@link Float}
     * @param hourFrom {@link Integer}
     * @param hourTo {@link Integer}
     * @return result as {@link TemperaturePeriod} or null if no data found by given criteria.
     */
    public TemperaturePeriod findLongestPeriodByTempRangeAndDayPeriod(Float lowTemp, Float highTemp, Integer hourFrom, Integer hourTo) {
        ArrayList<Object[]> periodsData = (ArrayList<Object[]>) temperatureRepository.findLongestPeriodByTempRangeAndDayPeriod(lowTemp, highTemp, hourFrom, hourTo);
        return getLongestPeriodFromData(periodsData);
    }

    /**
     * Method process raw data and select the longest period.
     * @param periodData The collection of raw data from database.
     * @return Longest period as {@link TemperaturePeriod} if found or null if not found.
     */
    private TemperaturePeriod getLongestPeriodFromData(ArrayList<Object[]> periodData) {
        if (!periodData.iterator().hasNext()) {
            return null;
        }
        ArrayList<TemperaturePeriod> temperaturePeriods = ConversionUtils.convertRawDataToTempPeriods(periodData);
        // Return longest period
        return Collections.max(temperaturePeriods);
    }

    /**
     * Sets all fields of Temperature entity which isn't null in object tempFrom into same fields in object tempTo.
     * @param tempFrom source instance of Temperature entity.
     * @param tempTo target instance of Temperature entity.
     */
    public void mergeTemperatures(Temperature tempFrom, Temperature tempTo) {
        ReflectionUtils.mergeNotNullFields(tempFrom, tempTo);
    }

}
