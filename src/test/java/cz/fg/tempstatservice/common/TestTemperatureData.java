package cz.fg.tempstatservice.common;

import cz.fg.tempstatservice.entities.Temperature;
import cz.fg.tempstatservice.utils.TestUtils;
import cz.fg.tempstatservice.utils.TimeUtils;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.LinkedList;

/**
 * Temperature data for testing purposes.
 */
@Getter
@Component
public class TestTemperatureData {

    private final int firstRecCount = 10;
    private final int middleRecCountA = 20;
    private final int middleRecCountB = 25;
    private final int lastRecCount = 30;
    private final int timeStep = 3;
    private final TemporalUnit timeUnit = ChronoUnit.MILLIS;
    private final TempRange firstTempRange = new TempRange(21F, 22F);
    private final TempRange middleTempRange = new TempRange(17F, 18F);
    private final TempRange lastTempRange = new TempRange(13F, 14F);
    private LinkedList<Temperature> firstTempList;
    private LinkedList<Temperature> middleTempListA;
    private LinkedList<Temperature> middleTempListB;
    private LinkedList<Temperature> lastTempList;
    private LinkedList<Temperature> allTemperatures;
    private LocalDateTime firstDate;
    private LocalDateTime middleDateA;
    private LocalDateTime middleDateB;
    private LocalDateTime lastDate;

    public TestTemperatureData() {
        initPeriods();
        allTemperatures = createTestTemperatureCollection();
    }

    private void initPeriods() {
        firstDate = TimeUtils.dateFromString("2019-03-21 12:00:00.000");
        middleDateA = TimeUtils.dateFromString("2019-03-22 15:00:00.000");
        middleDateB = TimeUtils.dateFromString("2019-03-22 18:00:00.000");
        lastDate = TimeUtils.dateFromString("2019-03-23 21:00:00.000");
    }

    private LinkedList<Temperature> createTestTemperatureCollection() {
        LinkedList<Temperature> temperatures = new LinkedList<>();
        firstTempList = TestUtils.createTestTemperatureCollection(firstDate, firstRecCount, timeStep, timeUnit, firstTempRange.getLow(), firstTempRange.getHigh());
        temperatures.addAll(firstTempList);
        middleTempListA = TestUtils.createTestTemperatureCollection(middleDateA, middleRecCountA, timeStep, timeUnit, middleTempRange.getLow(), middleTempRange.getHigh());
        temperatures.addAll(middleTempListA);
        middleTempListB = TestUtils.createTestTemperatureCollection(middleDateB, middleRecCountB, timeStep, timeUnit, middleTempRange.getLow(), middleTempRange.getHigh());
        temperatures.addAll(middleTempListB);
        lastTempList = TestUtils.createTestTemperatureCollection(lastDate, lastRecCount, timeStep, timeUnit, lastTempRange.getLow(), lastTempRange.getHigh());
        temperatures.addAll(lastTempList);
        return temperatures;
    }

}
