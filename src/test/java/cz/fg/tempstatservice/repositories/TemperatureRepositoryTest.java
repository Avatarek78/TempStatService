package cz.fg.tempstatservice.repositories;

import cz.fg.tempstatservice.configuration.MemDbTestConfig;
import cz.fg.tempstatservice.entities.Temperature;
import cz.fg.tempstatservice.entities.TemperaturePeriod;
import cz.fg.tempstatservice.utils.ConversionUtils;
import cz.fg.tempstatservice.utils.TestUtils;
import cz.fg.tempstatservice.utils.TimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Tests complex methods of {@link TemperatureRepository} by using H2 database in memory.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = { MemDbTestConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
@ActiveProfiles("test")
public class TemperatureRepositoryTest {

    private final Logger logger = LoggerFactory.getLogger(TemperatureRepositoryTest.class);
    private final int firstRecCount = 10;
    private final int middleRecCountA = 20;
    private final int middleRecCountB = 25;
    private final int lastRecCount = 30;
    private final int timeStep = 3;
    private final TimeUnit timeUnit = TimeUnit.MINUTES;
    private LinkedList<Temperature> firstTempRange;
    private LinkedList<Temperature> middleTempRangeA;
    private LinkedList<Temperature> middleTempRangeB;
    private LinkedList<Temperature> lastTempRange;
    private LinkedList<Temperature> allTemperatures;
    private Date firstDate;
    private Date middleDateA;
    private Date middleDateB;
    private Date lastDate;

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Before
    public void setUp() {
        initPeriods();
        allTemperatures = createTestTemperatureCollection();
        temperatureRepository.saveAll(allTemperatures);
    }

    private void initPeriods() {
        try {
            firstDate = TimeUtils.dateFormat.parse("2019-03-21 12:00:00.000");
            middleDateA = TimeUtils.dateFormat.parse("2019-03-22 15:00:00.000");
            middleDateB = TimeUtils.dateFormat.parse("2019-03-22 18:00:00.000");
            lastDate = TimeUtils.dateFormat.parse("2019-03-23 21:00:00.000");
        } catch (ParseException e) {
            logger.error("Date parse error", e);
        }
    }

    private LinkedList<Temperature> createTestTemperatureCollection() {
        LinkedList<Temperature> temperatures = new LinkedList<>();
        firstTempRange = TestUtils.createTestTemperatureCollection(firstDate, 1, firstRecCount, timeStep, timeUnit, 21F, 22F);
        temperatures.addAll(firstTempRange);
        middleTempRangeA = TestUtils.createTestTemperatureCollection(middleDateA, temperatures.size() + 1, middleRecCountA, timeStep, timeUnit, 17F, 18F);
        temperatures.addAll(middleTempRangeA);
        middleTempRangeB = TestUtils.createTestTemperatureCollection(middleDateB, temperatures.size() + 1, middleRecCountB, timeStep, timeUnit, 17F, 18F);
        temperatures.addAll(middleTempRangeB);
        lastTempRange = TestUtils.createTestTemperatureCollection(lastDate, temperatures.size() + 1, lastRecCount, timeStep, timeUnit, 13F, 14F);
        temperatures.addAll(lastTempRange);
        return temperatures;
    }

    /**
     * This test checks if method {@link TemperatureRepository#findLongestPeriodByTempRange} correctly select statistics data
     * from H2 database filled with test data.
     */
    @Test
    public void checkFindLongestPeriodByTempRange() {
        ArrayList<Object[]> periodsData = (ArrayList<Object[]>) temperatureRepository
                .findLongestPeriodByTempRange(21F, 22F);
        assertTemperaturePeriods(periodsData, firstRecCount, firstTempRange);
    }

    /**
     * This test checks if method {@link TemperatureRepository#findLongestPeriodByTempRangeAndDayPeriod} correctly select statistics data
     * from H2 database filled with test data.
     */
    @Test
    public void checkFindLongestPeriodByTempRangeAndDayPeriod() {
        ArrayList<Object[]> periodsData = (ArrayList<Object[]>) temperatureRepository
                .findLongestPeriodByTempRangeAndDayPeriod(17F, 18F, 18, 20);
        assertTemperaturePeriods(periodsData, middleRecCountB, middleTempRangeB);
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
                expectedTempData.getFirst().getDateAndTime().getTime(),
                period.getStartOfPeriod().getTime());
        assertEquals("Unexpected end of period date",
                expectedTempData.getLast().getDateAndTime().getTime(),
                period.getEndOfPeriod().getTime());
    }

}