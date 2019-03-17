package cz.fg.tempstatservice.controllers;

import cz.fg.tempstatservice.entities.Temperature;
import cz.fg.tempstatservice.exceptions.IdException;
import cz.fg.tempstatservice.services.MapValidationErrorService;
import cz.fg.tempstatservice.services.TemperatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Controller for handling of Temperatures data.
 */
@RestController
@RequestMapping("/TempStatService/temperatures")
public class TemperatureController {

    @Autowired
    private TemperatureService temperatureService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    private Logger logger = LoggerFactory.getLogger(TemperatureController.class);

    /**
     * Get all existing Temperature record from database.
     * @return collection of existing temperatures. Collection will be empty if no records in database found.
     */
    @GetMapping("")
    public Iterable<Temperature> getAllTemperatures() {
        return temperatureService.getAllTemperatures();
    }

    /**
     * Add new Temperature record.
     * @param temperature Temperature object to add.
     * @param result BindingResult for this request.
     * @return ResponseEntity with added Temperature.
     */
    @PostMapping("")
    public ResponseEntity<?> addTemperature(@Valid @RequestBody Temperature temperature, BindingResult result) {
        ResponseEntity<?> errorsMap = mapValidationErrorService.getErrorsMap(result);
        if (errorsMap != null) {
            return errorsMap;
        }
        Temperature savedTemperature = temperatureService.saveOrUpdateTemperature(temperature);
        HttpHeaders responseHeaders = createHeaderWithLocation("/temperatures/" + savedTemperature.getId());
        return new ResponseEntity<>(savedTemperature, responseHeaders, HttpStatus.CREATED);
    }

    /**
     * Get existing Temperature record by id.
     * @param id Temperature record id to find.
     * @return ResponseEntity with found Temperature record if exists.
     * @throws IdException when temperature record with specific id doesn't exists.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTemperatureById(@PathVariable Long id) throws IdException {
        Temperature temperature = temperatureService.getTemperatureById(id);
        HttpHeaders responseHeaders = createHeaderWithLocation("/temperatures/" + temperature.getId());
        return new ResponseEntity<>(temperature, responseHeaders, HttpStatus.OK);
    }

    /**
     * Update existing Temperature record.
     * @param id Temperature record id to find.
     * @param temperature Temperature entity with data to change.
     * @return ResponseEntity with updated Temperature record if exists.
     * @throws IdException when temperature record with specific id doesn't exists.
     */
    @PostMapping("/{id}")
    public ResponseEntity<?> updateTemperatureById(@PathVariable Long id, @RequestBody Temperature temperature) throws IdException {
        Temperature temperatureToUpdate = temperatureService.getTemperatureById(id);
        temperatureService.mergeTemperatures(temperature, temperatureToUpdate);
        Temperature updatedTemperature = temperatureService.saveOrUpdateTemperature(temperatureToUpdate);
        HttpHeaders responseHeaders = createHeaderWithLocation("/temperatures/" + updatedTemperature.getId());
        return new ResponseEntity<>(updatedTemperature, responseHeaders, HttpStatus.OK);
    }

    /**
     * Delete existing Temperature record.
     * @param id Temperature record id to find.
     * @return ResponseEntity with information message.
     * @throws IdException when temperature record with specific id doesn't exists.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTemperatureById(@PathVariable Long id) throws IdException {
        temperatureService.deleteTemperatureById(id);
        return new ResponseEntity<>("Temperature with ID: " + id + " was deleted", HttpStatus.OK);
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getLongestPeriodByMinAndMaxTemperature(@PathVariable Float lowTemp, @PathVariable Float highTemp) {
        //TODO: Vytvorit metody v TemperatureRepository a TemperatureService
        return null;
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getLongestPeriodByMinMaxTemperatureAndDayPeriod(@PathVariable Float lowTemp,
                                                                             @PathVariable Float highTemp,
                                                                             @PathVariable Short hourFrom,
                                                                             @PathVariable Short hourTo) {
        //TODO: Vytvorit metody v TemperatureRepository a TemperatureService
        return null;
    }

    /**
     * Common method for creating http header with specific location.
     * @param location location for header.
     * @return Created header object with location.
     */
    private HttpHeaders createHeaderWithLocation(String location) {
        HttpHeaders responseHeaders = new HttpHeaders();
        try {
            URI uri = new URI(location);
            responseHeaders.setLocation(uri);
        } catch (URISyntaxException e) {
            logger.warn("Set location failed: " ,e);
        }
        return responseHeaders;
    }

}
