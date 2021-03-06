package cz.fg.tempstatservice.controllers;

import cz.fg.tempstatservice.entities.ResponseMessage;
import cz.fg.tempstatservice.entities.Temperature;
import cz.fg.tempstatservice.entities.TemperaturePeriod;
import cz.fg.tempstatservice.exceptions.IdException;
import cz.fg.tempstatservice.services.MapValidationErrorService;
import cz.fg.tempstatservice.services.TemperatureService;
import cz.fg.tempstatservice.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

/**
 * Controller for handling with Temperatures data.
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
     * Get all existing {@link Temperature} records from database.
     * @return collection of existing temperatures. Collection will be empty if no records in database found.
     */
    @GetMapping("")
    public Iterable<Temperature> getAllTemperatures() {
        return temperatureService.getAllTemperatures();
    }

    /**
     * Add new {@link Temperature} record.
     * @param temperature {@link Temperature} object to add.
     * @param result BindingResult for this request.
     * @return {@link ResponseEntity} with added {@link Temperature}.
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
     * Get existing {@link Temperature} record by id.
     * @param id Temperature record id to find.
     * @return {@link ResponseEntity} with found {@link Temperature} record if exists.
     * @throws IdException when tempValue record with specific id doesn't exists.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTemperatureById(@PathVariable Long id) throws IdException {
        Temperature temperature = temperatureService.findByById(id);
        HttpHeaders responseHeaders = createHeaderWithLocation("/temperatures/" + temperature.getId());
        return new ResponseEntity<>(temperature, responseHeaders, HttpStatus.OK);
    }

    /**
     * Get {@link Temperature} records by temperature range.
     * @param lowTemp Temperature value as {@link Float}
     * @param highTemp Temperature value as {@link Float}
     * @return collection of found temperatures. Collection will be empty if no records by criteria found.
     */
    @GetMapping(path = "", params = {"lowTemp","highTemp"})
    public Iterable<Temperature> getTemperaturesByTempRange(@RequestParam Float lowTemp, @RequestParam Float highTemp) {
        return temperatureService.findByTempRange(lowTemp, highTemp);
    }

    /**
     * Get {@link Temperature} records by date and time range.
     * @param dateFrom LocalDateTime and time as {@link LocalDateTime}
     * @param dateTo LocalDateTime and time as {@link LocalDateTime}
     * @return collection of found temperatures. Collection will be empty if no records by criteria found.
     */
    @GetMapping(path = "", params = {"dateFrom","dateTo"})
    public Iterable<Temperature> getTemperaturesByDateAndTime(@RequestParam @DateTimeFormat(pattern = TimeUtils.DATE_FORMAT) LocalDateTime dateFrom,
                                                  @RequestParam @DateTimeFormat(pattern = TimeUtils.DATE_FORMAT) LocalDateTime dateTo) {
        return temperatureService.findByDateAndTime(dateFrom, dateTo);
    }

    /**
     * Update existing {@link Temperature} record.
     * @param id Temperature record id to find.
     * @param temperature {@link Temperature} entity with data to change. Only fields to change is required, other fields may be null.
     * @return {@link ResponseEntity} with updated {@link Temperature} record if exists.
     * @throws IdException when tempValue record with specific id doesn't exists.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTemperatureById(@PathVariable Long id, @RequestBody Temperature temperature) throws IdException {
        Temperature temperatureToUpdate = temperatureService.findByById(id);
        temperatureService.mergeTemperatures(temperature, temperatureToUpdate);
        Temperature updatedTemperature = temperatureService.saveOrUpdateTemperature(temperatureToUpdate);
        HttpHeaders responseHeaders = createHeaderWithLocation("/temperatures/" + updatedTemperature.getId());
        return new ResponseEntity<>(updatedTemperature, responseHeaders, HttpStatus.OK);
    }

    /**
     * Delete existing Temperature record.
     * @param id Temperature record id to find.
     * @return {@link ResponseEntity} with information message.
     * @throws IdException when temperature record with specific id doesn't exists.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTemperatureById(@PathVariable Long id) throws IdException {
        temperatureService.deleteTemperatureById(id);
        return new ResponseEntity<>(new ResponseMessage("Temperature with ID: " + id + " was deleted"), HttpStatus.OK);
    }

    /**
     * @see cz.fg.tempstatservice.services.TemperatureService#findLongestPeriodByTempRange(Float, Float)
     * @return {@link ResponseEntity} with statistics information represented by {@link TemperaturePeriod} object.
     */
    @GetMapping(path = "/statistics", params = {"lowTemp","highTemp"})
    public ResponseEntity<?> findLongestPeriodByTempRange(@RequestParam Float lowTemp, @RequestParam Float highTemp) {
        TemperaturePeriod periodForTemperatureRange = temperatureService.findLongestPeriodByTempRange(lowTemp, highTemp);
        return new ResponseEntity<>(periodForTemperatureRange, HttpStatus.OK);
    }

    /**
     * @see cz.fg.tempstatservice.services.TemperatureService#findLongestPeriodByTempRangeAndDayPeriod(Float, Float, Integer, Integer)
     * @return {@link ResponseEntity} with statistics information represented by {@link TemperaturePeriod} object.
     */
    @GetMapping(path = "/statistics", params = {"lowTemp","highTemp","hourFrom","hourTo"})
    public ResponseEntity<?> findLongestPeriodByTempRangeAndDayPeriod(@RequestParam Float lowTemp,
                                                                      @RequestParam Float highTemp,
                                                                      @RequestParam Integer hourFrom,
                                                                      @RequestParam Integer hourTo) {
        TemperaturePeriod periodForTemperatureRange = temperatureService.findLongestPeriodByTempRangeAndDayPeriod(lowTemp, highTemp, hourFrom, hourTo);
        return new ResponseEntity<>(periodForTemperatureRange, HttpStatus.OK);
    }

    /**
     * Common method for creating http header with specific URL location.
     * @param location location for header.
     * @return Created header object with location.
     */
    private HttpHeaders createHeaderWithLocation(String location) {
        HttpHeaders responseHeaders = new HttpHeaders();
        try {
            URI uri = new URI(location);
            responseHeaders.setLocation(uri);
        } catch (URISyntaxException e) {
            logger.warn("Set URL location failed: " ,e);
        }
        return responseHeaders;
    }

}
