package cz.fg.tempstatservice;

import cz.fg.tempstatservice.entities.Temperature;
import cz.fg.tempstatservice.repositories.TemperatureRepository;
import cz.fg.tempstatservice.services.TemperatureService;
import cz.fg.tempstatservice.utils.TestUtils;
import cz.fg.tempstatservice.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class TempStatServiceApplication {

	private final Logger logger = LoggerFactory.getLogger(TempStatServiceApplication.class);

	@Autowired
	private TemperatureRepository temperatureRepository;

	@Autowired
	private TemperatureService temperatureService;

	private Date firstDate;
	private Date middleDate;
	private Date lastDate;
	private Date nextDate;

	public static void main(String[] args) {
		SpringApplication.run(TempStatServiceApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void afterStartup() {
		logger.info("TempStatServiceApplication started...");
//		createDbTestData();
//		Iterable<Object[]> period = temperatureRepository.findLongestPeriodByTempRange(17F, 18F);
//		period.forEach(obj -> logger.info(Arrays.stream(obj)
//				.filter(Objects::nonNull)
//				.map(o -> o.toString())
//				.collect(Collectors.joining(","))));

//		TemperaturePeriod period = temperatureService.findLongestPeriodByTempRange(17F, 18F);
//		logger.info("longest period by temp range 17-18: " + period.toString());
//		period = temperatureService.findLongestPeriodByTempRangeAndDayPeriod(17F, 18F, 16, 17);
//		logger.info("longest period by temp range 17-18 and hour range 14-15: " + period.toString());
	}

	private void createDbTestData() {
		initPeriods();
		LinkedList<Temperature> temperatures = TestUtils.createTestTemperatureCollection(
				firstDate, 1, 1000, 10, TimeUnit.SECONDS, 15F, 20F);

		temperatureRepository.deleteAll();
		temperatureRepository.saveAll(temperatures);
		logger.info("count=" + temperatureRepository.count());
		Iterable<Temperature> byTempRange = temperatureRepository.findByTempRange(22F, 24F);
		logger.info("byTempRange=" + byTempRange.spliterator().estimateSize());
	}

	private void initPeriods() {
		try {
			firstDate = TimeUtils.dateFormat.parse("2019-03-21 15:00:00.000");
			middleDate = TimeUtils.dateFormat.parse("2019-03-22 16:00:00.000");
			lastDate = TimeUtils.dateFormat.parse("2019-03-23 17:00:00.000");
			nextDate = TimeUtils.dateFormat.parse("2019-03-24 18:00:00.000");
		} catch (ParseException e) {
			logger.error("Date parse error", e);
		}
	}

}