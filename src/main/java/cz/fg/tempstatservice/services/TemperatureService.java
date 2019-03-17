package cz.fg.tempstatservice.services;

import cz.fg.tempstatservice.entities.Temperature;
import cz.fg.tempstatservice.exceptions.IdException;
import cz.fg.tempstatservice.repositories.TemperatureRepository;
import cz.fg.tempstatservice.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/**
 * Service class for all functionality around Temperature.
 */
@Service
public class TemperatureService {

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
    public Temperature getTemperatureById(Long id) throws IdException {
        return findTemperatureById(id);
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
     * Sets all fields of Temperature entity which isn't null in object tempFrom into same fields in object tempTo.
     * @param tempFrom source instance of Temperature entity.
     * @param tempTo target instance of Temperature entity.
     */
    public void mergeTemperatures(Temperature tempFrom, Temperature tempTo) {
        ReflectionUtils.mergeNotNullFields(tempFrom, tempTo);
    }

    private Temperature findTemperatureById(Long id) throws IdException {
        Temperature temperature = temperatureRepository.findById(id).orElse(null);
        if (temperature == null) {
            throw new IdException("Temperature with ID '" + id + "' doesn't exists");
        }
        return temperature;
    }

}
