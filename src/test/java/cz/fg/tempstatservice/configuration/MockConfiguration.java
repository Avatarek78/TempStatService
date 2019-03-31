package cz.fg.tempstatservice.configuration;

import cz.fg.tempstatservice.repositories.TemperatureRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.easymock.EasyMock;
import org.springframework.context.annotation.Profile;

/**
 * Configuration for JUnit testing.
 */
@Configuration()
@Profile("JUnitTest")
public class MockConfiguration {
    @Bean
    public TemperatureRepository temperatureRepository() {
        return EasyMock.mock(TemperatureRepository.class);
    }
}

