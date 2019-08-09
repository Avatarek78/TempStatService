package cz.fg.tempstatservice.repositories;

import cz.fg.tempstatservice.common.TempRange;
import cz.fg.tempstatservice.common.TestTemperatureData;
import cz.fg.tempstatservice.configuration.MemDbTestConfig;
import cz.fg.tempstatservice.entities.Temperature;
import cz.fg.tempstatservice.entities.TemperaturePeriod;
import cz.fg.tempstatservice.utils.ConversionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Tests complex methods of {@link TemperatureRepository} by using H2 database in memory.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MemDbTestConfig.class, TestTemperatureData.class }, loader = AnnotationConfigContextLoader.class)
public class TemperatureRepositoryTest {

    private final Logger logger = LoggerFactory.getLogger(TemperatureRepositoryTest.class);

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private TestTemperatureData data;

    @Before
    public void setUp() {
        // Store test data into test database only if database is empty.
        if (temperatureRepository.count() == 0) {
            temperatureRepository.saveAll(data.getAllTemperatures());
        }
    }

    /**
     * This test checks if method {@link TemperatureRepository#findLongestPeriodByTempRange} correctly select statistics data
     * from H2 database filled with test data.
     */
    @Test
    public void checkFindLongestPeriodByTempRange() {
        TempRange tempRange = data.getFirstTempRange();
        ArrayList<Object[]> periodsData = (ArrayList<Object[]>) temperatureRepository
                .findLongestPeriodByTempRange(tempRange.getLow(), tempRange.getHigh());
        assertTemperaturePeriods(periodsData, data.getFirstRecCount(), data.getFirstTempList());
    }

    /**
     * This test checks if method {@link TemperatureRepository#findLongestPeriodByTempRangeAndDayPeriod} correctly select statistics data
     * from H2 database filled with test data.
     */
    @Test
    public void checkFindLongestPeriodByTempRangeAndDayPeriod() {
        TempRange tempRange = data.getMiddleTempRange();
        ArrayList<Object[]> periodsData = (ArrayList<Object[]>) temperatureRepository
                .findLongestPeriodByTempRangeAndDayPeriod(tempRange.getLow(), tempRange.getHigh(), 18, 20);
        assertTemperaturePeriods(periodsData, data.getMiddleRecCountB(), data.getMiddleTempListB());
    }

    @Test
    public void checkFindByTempRange() {
        TempRange tempRange = data.getLastTempRange();
        Iterable<Temperature> byTempRange = temperatureRepository.findByTempRange(tempRange.getLow(), tempRange.getHigh());
        assertEquals("Unexpected count of results", data.getLastTempList().size(), byTempRange.spliterator().estimateSize());
    }

    @Test
    public void checkFindByDateAndTime() {
        Iterable<Temperature> byTempRange = temperatureRepository.findByDateAndTime(data.getFirstTempList().getFirst().getDateAndTime(),
                data.getFirstTempList().getLast().getDateAndTime());
        assertEquals("Unexpected count of results", data.getFirstTempList().size(), byTempRange.spliterator().estimateSize());
    }

    private void assertTemperaturePeriods(ArrayList<Object[]> periodsData,
                                          int expectedRecCount,
                                          LinkedList<Temperature> expectedTempData) {
        ArrayList<TemperaturePeriod> tempPeriods = ConversionUtils.convertRawDataToTempPeriods(periodsData);
        assertEquals("Unexpected count of results", tempPeriods.size(), 1);
        TemperaturePeriod period = tempPeriods.get(0);
        logger.info(period.toString());
        assertEquals("Unexpected measurements count", expectedRecCount, period.getCountOfMeasurements().intValue());
        assertEquals("Unexpected start of period date",
                expectedTempData.getFirst().getDateAndTime().getNano(),
                period.getStartOfPeriod().getNano());
        assertEquals("Unexpected end of period date",
                expectedTempData.getLast().getDateAndTime().getNano(),
                period.getEndOfPeriod().getNano());
    }

}