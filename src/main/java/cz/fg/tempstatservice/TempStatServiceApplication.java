package cz.fg.tempstatservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class TempStatServiceApplication {

	private final Logger logger = LoggerFactory.getLogger(TempStatServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TempStatServiceApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void afterStartup() {
		logger.info("TempStatServiceApplication started...");
	}
}