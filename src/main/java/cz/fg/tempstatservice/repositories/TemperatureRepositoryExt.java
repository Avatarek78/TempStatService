package cz.fg.tempstatservice.repositories;

import cz.fg.tempstatservice.entities.Temperature;
import org.springframework.stereotype.Repository;

/**
 * Interface extending TemperatureRepository interface. Primarily for demonstration of using CriteriaApi
 * except of direct queries in TemperatureRepository interface.
 */
@Repository
public interface TemperatureRepositoryExt {

    /**
     * Find {@link Temperature} records by temperature values range. Method demonstrate extension of
     * Spring PagingAndSortingRepository interface by functions implemented by using Criteria Api.
     * @param lowTemp
     * @param highTemp
     * @return Collection of {@link Temperature}
     */
    Iterable<Temperature> findByTempRange(Float lowTemp, Float highTemp);

}
