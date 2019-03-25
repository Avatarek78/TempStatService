package cz.fg.tempstatservice.services;

import cz.fg.tempstatservice.configuration.MockConfiguration;
import cz.fg.tempstatservice.entities.Temperature;
import cz.fg.tempstatservice.entities.TemperaturePeriod;
import cz.fg.tempstatservice.repositories.TemperatureRepository;
import cz.fg.tempstatservice.utils.TimeUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Tests complex methods of {@link TemperatureService}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        TemperatureService.class,
        MockConfiguration.class
})
@ActiveProfiles("test")
public class TemperatureServiceTest {

    private final Logger logger = LoggerFactory.getLogger(TemperatureServiceTest.class);

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private TemperatureService temperatureService;

    private Date expEndOfLongestPeriod;

    @Before
    public void setUp() {
        mockTemperatureRepository();
    }

    private void mockTemperatureRepository() {
        ArrayList<Object[]> rawPeriodData = createTestRawPeriodDataCollection();
        EasyMock.expect(temperatureRepository.count()).andReturn((long) rawPeriodData.size()).anyTimes();
        EasyMock.expect(temperatureRepository.findLongestPeriodByTempRange(EasyMock.anyFloat(), EasyMock.anyFloat()))
                .andReturn(rawPeriodData).anyTimes();
        EasyMock.expect(temperatureRepository.findLongestPeriodByTempRangeAndDayPeriod(EasyMock.anyFloat(), EasyMock.anyFloat(), EasyMock.anyInt(), EasyMock.anyInt()))
                .andReturn(rawPeriodData).anyTimes();
        EasyMock.replay(temperatureRepository);
    }

    private ArrayList<Object[]> createTestRawPeriodDataCollection() {
        ArrayList<Object[]> dataList = new ArrayList<>();
        Object[] dataArr = new Object[5];
        /**
         * [0] - {@link java.sql.Timestamp} Start date and time.
         * [1] - {@link java.sql.Timestamp} End date and time.
         * [2] - {@link Float} Value of minimum temperature.
         * [3] - {@link Float} Value of maximum temperature.
         * [4] - {@link String} Count of measurements.
         */
        try {
            dataArr[0] = TimeUtils.dateFormat.parse("2019-03-20 12:00:00.000");
            dataArr[1] = TimeUtils.dateFormat.parse("2019-03-20 13:00:00.000");
            dataArr[2] = 16f;
            dataArr[3] = 17f;
            dataArr[4] = BigInteger.valueOf(5L);
            dataList.add(dataArr);
            dataArr = Arrays.copyOf(dataArr, dataArr.length);
            // End date of longest  period longer by one millisecond.
            expEndOfLongestPeriod = TimeUtils.dateFormat.parse("2019-03-20 13:00:00.001");
            dataArr[1] = expEndOfLongestPeriod;
            dataList.add(dataArr);
            dataArr = Arrays.copyOf(dataArr, dataArr.length);
            dataArr[1] = TimeUtils.dateFormat.parse("2019-03-20 12:59:59.999");
            dataList.add(dataArr);
        } catch (ParseException e) {
            logger.error("Date parse error", e);
        }
        return dataList;
    }

    /**
     * This test checks if {@link TemperatureService} select correct {@link TemperaturePeriod} from test collection of {@link Temperature} entities.
     * Similar method findLongestPeriodByTempRangeAndDayPeriod isn't necessary to test because difference of this method is implemented
     * in {@link TemperatureRepository}.
     */
    @Test
    public void checkFindLongestPeriodByTempRange() {
        TemperaturePeriod period = temperatureService.findLongestPeriodByTempRange(15F, 18F);
        logger.info(period.toString());
        assertNotNull("Period must be not null", period);
        assertEquals("Unexpected endOfPeriod date", period.getEndOfPeriod(), expEndOfLongestPeriod);
    }
}